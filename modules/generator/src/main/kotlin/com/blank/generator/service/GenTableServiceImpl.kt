package com.blank.generator.service

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.constant.Constants
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.StreamUtils
import com.blank.common.json.utils.JsonUtils.parseMap
import com.blank.common.json.utils.JsonUtils.toJsonString
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.mybatis.helper.DataBaseHelper
import com.blank.common.satoken.utils.LoginHelper
import com.blank.generator.constant.GenConstants
import com.blank.generator.domain.GenTable
import com.blank.generator.domain.GenTableColumn
import com.blank.generator.domain.table.GenTableColumnDef.GEN_TABLE_COLUMN
import com.blank.generator.domain.table.GenTableDef.GEN_TABLE
import com.blank.generator.mapper.GenTableColumnMapper
import com.blank.generator.mapper.GenTableMapper
import com.blank.generator.util.GenUtils
import com.blank.generator.util.VelocityInitializer
import com.blank.generator.util.VelocityUtils
import com.mybatisflex.core.datasource.DataSourceKey
import com.mybatisflex.core.keygen.impl.FlexIDKeyGenerator
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.*
import com.mybatisflex.core.row.Db
import org.apache.commons.lang3.StringUtils
import org.apache.velocity.app.Velocity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.StringWriter
import java.nio.charset.StandardCharsets
import java.util.function.Consumer
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream


/**
 * 代码生成 服务层实现
 */
@Slf4j
@Service
class GenTableServiceImpl(
    private val baseMapper: GenTableMapper,
    private val genTableColumnMapper: GenTableColumnMapper
) : IGenTableService {


    private val snowflake = FlexIDKeyGenerator()

    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    override fun selectGenTableColumnListByTableId(tableId: Long): MutableList<GenTableColumn> {
        return genTableColumnMapper.selectListByQuery(QueryWrapper.create().select()
            .where(GEN_TABLE_COLUMN.TABLE_ID.eq(tableId))
            .orderBy(GEN_TABLE_COLUMN.SORT, true))
    }

    /**
     * 查询业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    override fun selectGenTableById(id: Long): GenTable? {
        val genTable = baseMapper.selectOneWithRelationsById(id)
        setTableFromOptions(genTable)
        return genTable
    }

    override fun selectPageGenTableList(genTable: GenTable, pageQuery: PageQuery): TableDataInfo<GenTable> {
        val queryWrapper = buildGenTableQueryWrapper(genTable)
        val page = baseMapper.paginate(pageQuery, queryWrapper)
        return TableDataInfo.build(page)
    }

    private fun buildGenTableQueryWrapper(genTable: GenTable): QueryWrapper {
        val params = genTable.params

        return QueryWrapper.create().from(GEN_TABLE)
            .where(GEN_TABLE.DATA_NAME.eq(genTable.dataName, StrUtil.isNotBlank(genTable.dataName)))
            .and(QueryMethods.lower(GEN_TABLE.TABLE_NAME).like(StringUtils.lowerCase(genTable.tableName),
                StrUtil.isNotBlank(genTable.tableName)))
            .and(QueryMethods.lower(GEN_TABLE.TABLE_COMMENT).like(StringUtils.lowerCase(genTable.tableComment),
                StrUtil.isNotBlank(genTable.tableComment)))
            .and(GEN_TABLE.CREATE_TIME.between(params["beginTime"], params["endTime"],
                params["beginTime"] != null && params["endTime"] != null))
    }

    override fun selectPageDbTableList(genTable: GenTable, pageQuery: PageQuery): TableDataInfo<GenTable> {
        return try {
            DataSourceKey.use(genTable.dataName)
            val value: MutableList<String> = baseMapper.selectTableNameList(genTable.dataName!!)
            genTable.params["genTableNames"] = value
            val page = selectPageDbTableList(pageQuery.build(), genTable)
            TableDataInfo.build(page)
        } finally {
            DataSourceKey.clear()
        }
    }

    private fun selectPageDbTableList(page: Page<GenTable>, genTable: GenTable): Page<GenTable> {
        val genTableNames = genTable.params["genTableNames"] as MutableList<String>
        val tableName = StringUtils.lowerCase(genTable.tableName)
        val tableComment = StringUtils.lowerCase(genTable.tableComment)
        if (DataBaseHelper.isMySql()) {
            val queryWrapper = QueryWrapper.create()
                .select("table_name", "table_comment", "create_time", "update_time")
                .from("information_schema.tables")
                .where("table_schema = (select database())")
                .and("table_name NOT LIKE 'pj_%' AND table_name NOT LIKE 'gen_%'")
                .and(QueryMethods.column("table_name").notIn(
                    genTableNames
                ) { collection: List<String?>? ->
                    If.isNotEmpty(
                        collection
                    )
                })
                .and(QueryMethods.column("lower(table_name)").like(tableName))
                .and(QueryMethods.column("lower(table_comment)").like(tableComment))
                .orderBy("create_time", false)
            return baseMapper.paginate(page, queryWrapper)
        }
        if (DataBaseHelper.isOracle()) {
            val queryWrapper = QueryWrapper.create()
                .select(
                    QueryColumn("lower(dt.table_name)").`as`("table_name"),
                    QueryColumn("dtc.comments").`as`("table_comment"),
                    QueryColumn("uo.created").`as`("create_time"),
                    QueryColumn("uo.last_ddl_time").`as`("update_time")
                )
                .from(
                    QueryTable("user_tables").`as`("dt"),
                    QueryTable("user_tab_comments").`as`("dtc"),
                    QueryTable("user_objects").`as`("uo")
                )
                .where("dt.table_name = dtc.table_name and dt.table_name = uo.object_name and uo.object_type = 'TABLE'")
                .and("dt.table_name NOT LIKE 'pj_%' AND dt.table_name NOT LIKE 'GEN_%'")
                .and(QueryMethods.column("lower(dt.table_name)").notIn(
                    genTableNames
                ) { collection: List<String?>? ->
                    If.isNotEmpty(
                        collection
                    )
                })
                .and(QueryMethods.column("lower(dt.table_name)").like(tableName))
                .and(QueryMethods.column("lower(dtc.comments)").like(tableComment))
                .orderBy("create_time", false)
            return baseMapper.paginate(page, queryWrapper)
        }
        if (DataBaseHelper.isPostgerSql()) {
            val queryWrapper = QueryWrapper.create()
                .with<QueryWrapper>("list_table").asRaw(
                    """
                    SELECT c.relname AS table_name,
                                            obj_description(c.oid) AS table_comment,
                                            CURRENT_TIMESTAMP AS create_time,
                                            CURRENT_TIMESTAMP AS update_time
                                    FROM pg_class c
                                        LEFT JOIN pg_namespace n ON n.oid = c.relnamespace
                                    WHERE (c.relkind = ANY (ARRAY ['r'::"char", 'p'::"char"]))
                                        AND c.relname != 'spatial_%'::text
                                        AND n.nspname = 'public'::name
                                        AND n.nspname <![CDATA[ <> ]]> ''::name

                    """.trimIndent()
                )
                .select(
                    QueryColumn("c.relname").`as`("table_name"),
                    QueryColumn("obj_description(c.oid)").`as`("table_comment"),
                    QueryColumn("CURRENT_TIMESTAMP").`as`("create_time"),
                    QueryColumn("CURRENT_TIMESTAMP").`as`("update_time")
                )
                .from("list_table")
                .where("table_name NOT LIKE 'pj_%' AND table_name NOT LIKE 'gen_%'")
                .and(QueryMethods.column("table_name").notIn(
                    genTableNames
                ) { collection: List<String>? ->
                    If.isNotEmpty(
                        collection
                    )
                })
                .and(QueryMethods.lower("table_name").like(tableName))
                .and(QueryMethods.lower("table_comment").like(tableComment))
                .orderBy("create_time", false)
            return baseMapper.paginate(page, queryWrapper)
        }
        if (DataBaseHelper.isSqlServer()) {
            val queryWrapper = QueryWrapper.create()
                .select(
                    QueryColumn("cast(D.NAME as nvarchar)").`as`("table_name"),
                    QueryColumn("cast(F.VALUE as nvarchar)").`as`("table_comment"),
                    QueryColumn("crdate").`as`("create_time"),
                    QueryColumn("refdate").`as`("update_time")
                )
                .from(QueryTable("SYSOBJECTS").`as`("D"))
                .innerJoin<QueryWrapper>("SYS.EXTENDED_PROPERTIES F")
                .on("D.ID = F.MAJOR_ID")
                .where("F.MINOR_ID = 0 AND D.XTYPE = 'U' AND D.NAME != 'DTPROPERTIES' AND D.NAME NOT LIKE 'pj_%' AND D.NAME NOT LIKE 'gen_%'")
                .and(QueryMethods.column("D.NAME").notIn(
                    genTableNames
                ) { collection: List<String>? ->
                    If.isNotEmpty(
                        collection
                    )
                })
                .and(QueryMethods.lower("D.NAME").like(tableName))
                .and(QueryMethods.lower("CAST(F.VALUE AS nvarchar)").like(tableComment))
                .orderBy("crdate", false)
            return baseMapper.paginate(page, queryWrapper)
        }
        throw ServiceException("不支持的数据库类型")
    }

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @param dataName   数据源名称
     * @return 数据库表集合
     */
    override fun selectDbTableListByNames(tableNames: Array<String>, dataName: String): MutableList<GenTable> {
        return try {
            DataSourceKey.use(dataName)
            baseMapper.selectDbTableListByNames(tableNames)
        } finally {
            DataSourceKey.clear()
        }
    }

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    override fun selectGenTableAll(): MutableList<GenTable> {
        return baseMapper.selectGenTableAll()
    }

    /**
     * 修改业务
     *
     * @param genTable 业务信息
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateGenTable(genTable: GenTable) {
        val options: String? = toJsonString(genTable.params)
        genTable.options = options
        val row = baseMapper.update(genTable)
        if (row > 0) {
            for (cenTableColumn in genTable.columns!!) {
                genTableColumnMapper.update(cenTableColumn)
            }
        }
    }

    /**
     * 删除业务对象
     *
     * @param tableIds 需要删除的数据ID
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteGenTableByIds(tableIds: Array<Long>) {
        val ids = listOf(*tableIds)
        baseMapper.deleteBatchByIds(ids)
        genTableColumnMapper.deleteByQuery(
            QueryWrapper.create().from(GEN_TABLE_COLUMN)
                .where(GEN_TABLE_COLUMN.TABLE_ID.`in`(ids))
        )
    }

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     * @param dataName  数据源名称
     */
    @Transactional
    override fun importGenTable(tableList: MutableList<GenTable>, dataName: String) {
        val operId: Long = LoginHelper.getUserId()!!
        try {
            for (table in tableList) {
                val tableName: String = table.tableName!!
                GenUtils.initTable(table, operId)
                table.dataName = dataName
                val row = baseMapper.insert(table, true)
                if (row > 0) {
                    // 保存列信息
                    try {
                        DataSourceKey.use(dataName)
                        val genTableColumns: MutableList<GenTableColumn> =
                            genTableColumnMapper.selectDbTableColumnsByName(tableName)
                        val saveColumns: MutableList<GenTableColumn> = ArrayList()
                        for (column in genTableColumns) {
                            GenUtils.initColumnField(column, table)
                            saveColumns.add(column)
                        }
                        if (CollUtil.isNotEmpty(saveColumns)) {
                            genTableColumnMapper.insertBatch(saveColumns)
                        }
                    } finally {
                        DataSourceKey.clear()
                    }
                }
            }
        } catch (e: java.lang.Exception) {
            throw ServiceException("导入失败：${e.message}")
        }
    }

    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    override fun previewCode(tableId: Long): MutableMap<String, String> {
        val dataMap: MutableMap<String, String> = LinkedHashMap()
        // 查询表信息
        val table = baseMapper.selectOneWithRelationsById(tableId)
        val menuIds: MutableList<Long> = ArrayList()
        for (i in 0..5) {
            menuIds.add(snowflake.generate(null, null).toString().toLong())
        }
        table.menuIds = menuIds
        // 设置主键列信息
        setPkColumn(table)
        VelocityInitializer.initVelocity()

        val context = VelocityUtils.prepareContext(table)

        // 获取模板列表
        val templates = VelocityUtils.getTemplateList(table.tplCategory!!)
        for (template in templates) {
            // 渲染模板
            val sw = StringWriter()
            val tpl = Velocity.getTemplate(template, Constants.UTF8)
            tpl.merge(context, sw)
            dataMap[template] = sw.toString()
        }
        return dataMap
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableId 表名称
     * @return 数据
     */
    override fun downloadCode(tableId: Long): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val zip = ZipOutputStream(outputStream)
        generatorCode(tableId, zip)
        IoUtil.close(zip)
        return outputStream.toByteArray()
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableId 表名称
     */
    override fun generatorCode(tableId: Long) {
        // 查询表信息
        val table = baseMapper.selectOneWithRelationsById(tableId)
        // 设置主键列信息
        setPkColumn(table)
        VelocityInitializer.initVelocity()
        val context = VelocityUtils.prepareContext(table)

        // 获取模板列表
        val templates = VelocityUtils.getTemplateList(table.tplCategory!!)
        for (template in templates) {
            if (!StringUtils.containsAny(
                    template,
                    "sql.vm",
                    "api.ts.vm",
                    "types.ts.vm",
                    "index.vue.vm",
                    "index-tree.vue.vm"
                )
            ) {
                // 渲染模板
                val sw = StringWriter()
                val tpl = Velocity.getTemplate(template, Constants.UTF8)
                tpl.merge(context, sw)
                try {
                    val path = getGenPath(table, template)
                    FileUtil.writeUtf8String(sw.toString(), path)
                } catch (e: Exception) {
                    throw ServiceException("渲染模板失败，表名：${table.tableName}")
                }
            }
        }
    }

    /**
     * 同步数据库
     *
     * @param tableId 表名称
     */
    @Transactional
    override fun synchDb(tableId: Long) {
        val table = baseMapper.selectOneWithRelationsById(tableId)
        val tableColumns: MutableList<GenTableColumn> = table.columns!!
        val tableColumnMap: Map<String?, GenTableColumn> =
            StreamUtils.toIdentityMap(tableColumns, GenTableColumn::columnName)
        val dbTableColumns: MutableList<GenTableColumn>? = try {
            DataSourceKey.use(table.dataName)
            genTableColumnMapper.selectDbTableColumnsByName(table.tableName!!)
        } finally {
            DataSourceKey.clear()
        }
        if (CollUtil.isEmpty(dbTableColumns)) {
            throw ServiceException("同步数据失败，原表结构不存在")
        }
        val dbTableColumnNames: MutableList<String?> = StreamUtils.toList(dbTableColumns!!, GenTableColumn::columnName)

        val saveColumns: MutableList<GenTableColumn> = ArrayList()
        dbTableColumns.forEach(Consumer { column: GenTableColumn ->
            GenUtils.initColumnField(column, table)
            if (tableColumnMap.containsKey(column.columnName)) {
                val prevColumn = tableColumnMap[column.columnName]!!
                column.columnId = prevColumn.columnId
                if (column.isList()) {
                    // 如果是列表，继续保留查询方式/字典类型选项
                    column.dictType = prevColumn.dictType
                    column.queryType = prevColumn.queryType
                }
                if (StringUtils.isNotEmpty(prevColumn.isRequired) && !column.isPk()
                    && (column.isInsert() || column.isEdit())
                    && (column.isUsableColumn || !column.isSuperColumn)
                ) {
                    // 如果是(新增/修改&非主键/非忽略及父属性)，继续保留必填/显示类型选项
                    column.isRequired = prevColumn.isRequired
                    column.htmlType = prevColumn.htmlType
                }
            }
            saveColumns.add(column)
        })
        if (CollUtil.isNotEmpty(saveColumns)) {
            // genTableColumnMapper.insertBatch(saveColumns)
            Db.executeBatch(saveColumns, 1000, GenTableColumnMapper::class.java) { mapper, index ->
                mapper.insertOrUpdate(index)
            }
        }
        val delColumns: MutableList<GenTableColumn> = StreamUtils.filter(tableColumns) { column ->
            !dbTableColumnNames.contains(
                column.columnName
            )
        }
        if (CollUtil.isNotEmpty(delColumns)) {
            val ids: MutableList<Long?> = StreamUtils.toList(delColumns, GenTableColumn::columnId)
            if (CollUtil.isNotEmpty(ids)) {
                genTableColumnMapper.deleteBatchByIds(ids)
            }
        }
    }

    /**
     * 批量生成代码（下载方式）
     *
     * @param tableIds 表ID数组
     * @return 数据
     */
    override fun downloadCode(tableIds: Array<String>): ByteArray {
        val outputStream = ByteArrayOutputStream()
        val zip = ZipOutputStream(outputStream)
        for (tableId in tableIds) {
            generatorCode(tableId.toLong(), zip)
        }
        IoUtil.close(zip)
        return outputStream.toByteArray()
    }

    /**
     * 查询表信息并生成代码
     */
    private fun generatorCode(tableId: Long, zip: ZipOutputStream) {
        // 查询表信息
        val table = baseMapper.selectOneWithRelationsById(tableId)
        val menuIds: MutableList<Long> = ArrayList()
        for (i in 0..5) {
            menuIds.add(snowflake.generate(null, null).toString().toLong())
        }
        table.menuIds = menuIds
        // 设置主键列信息
        setPkColumn(table)
        VelocityInitializer.initVelocity()
        val context = VelocityUtils.prepareContext(table)

        // 获取模板列表
        val templates = VelocityUtils.getTemplateList(table.tplCategory!!)
        for (template in templates) {
            // 渲染模板
            val sw = StringWriter()
            val tpl = Velocity.getTemplate(template, Constants.UTF8)
            tpl.merge(context, sw)
            try {
                // 添加到zip
                zip.putNextEntry(ZipEntry(VelocityUtils.getFileName(template, table)))
                IoUtil.write(zip, StandardCharsets.UTF_8, false, sw.toString())
                IoUtil.close(sw)
                zip.flush()
                zip.closeEntry()
            } catch (e: IOException) {
                log.error(e) { "渲染模板失败，表名：${table.tableName}" }
            }
        }
    }

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    override fun validateEdit(genTable: GenTable) {
        if (GenConstants.TPL_TREE == genTable.tplCategory) {
            val options = toJsonString(genTable.params)
            val paramsObj = parseMap(options)!!
            if (StringUtils.isEmpty(paramsObj.getStr(GenConstants.TREE_CODE))) {
                throw ServiceException("树编码字段不能为空")
            } else if (StringUtils.isEmpty(paramsObj.getStr(GenConstants.TREE_PARENT_CODE))) {
                throw ServiceException("树父编码字段不能为空")
            } else if (StringUtils.isEmpty(paramsObj.getStr(GenConstants.TREE_NAME))) {
                throw ServiceException("树名称字段不能为空")
            }
        }
    }

    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     */
    fun setPkColumn(table: GenTable) {
        if (ObjectUtil.isNotNull(table.columns)) {
            for (column in table.columns!!) {
                if (column.isPk()) {
                    table.pkColumn = column
                    break
                }
            }

            if (ObjectUtil.isNull(table.pkColumn)) {
                table.pkColumn = table.columns!![0]
            }
        }
    }

    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    fun setTableFromOptions(genTable: GenTable) {
        val paramsObj = parseMap(genTable.options)
        if (ObjectUtil.isNotNull(paramsObj)) {
            val treeCode = paramsObj!!.getStr(GenConstants.TREE_CODE)
            val treeParentCode = paramsObj.getStr(GenConstants.TREE_PARENT_CODE)
            val treeName = paramsObj.getStr(GenConstants.TREE_NAME)
            val parentMenuId = paramsObj.getStr(GenConstants.PARENT_MENU_ID)
            val parentMenuName = paramsObj.getStr(GenConstants.PARENT_MENU_NAME)

            genTable.treeCode = treeCode
            genTable.treeParentCode = treeParentCode
            genTable.treeName = treeName
            genTable.parentMenuId = parentMenuId
            genTable.parentMenuName = parentMenuName
        }
    }

    /**
     * 获取代码生成地址
     *
     * @param table    业务表信息
     * @param template 模板文件路径
     * @return 生成地址
     */
    fun getGenPath(table: GenTable, template: String): String {
        val genPath = table.genPath
        return if (StringUtils.equals(genPath, "/")) {
            System.getProperty("user.dir") + File.separator + "src" + File.separator + VelocityUtils.getFileName(
                template,
                table
            )
        } else genPath + File.separator + VelocityUtils.getFileName(template, table)
    }
}
