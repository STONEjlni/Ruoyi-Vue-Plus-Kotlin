package com.blank.generator.util

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.lang.Dict
import com.blank.common.core.utils.DateUtils.getDate
import com.blank.common.core.utils.StringUtilsExtend.format
import com.blank.common.core.utils.StringUtilsExtend.toCamelCase
import com.blank.common.json.utils.JsonUtils.parseMap
import com.blank.common.mybatis.helper.DataBaseHelper.isOracle
import com.blank.common.mybatis.helper.DataBaseHelper.isPostgerSql
import com.blank.common.mybatis.helper.DataBaseHelper.isSqlServer
import com.blank.generator.constant.GenConstants
import com.blank.generator.domain.GenTable
import com.blank.generator.domain.GenTableColumn
import org.apache.commons.lang3.StringUtils
import org.apache.velocity.VelocityContext

/**
 * 模板处理工具类
 */
object VelocityUtils {
    /**
     * 项目空间路径
     */
    private const val PROJECT_PATH = "main/java"

    /**
     * mybatis空间路径
     */
    private const val MYBATIS_PATH = "main/resources/mapper"

    /**
     * 默认上级菜单，系统工具
     */
    private const val DEFAULT_PARENT_MENU_ID = "3"

    /**
     * 设置模板变量信息
     *
     * @return 模板列表
     */
    fun prepareContext(genTable: GenTable): VelocityContext {
        val moduleName = genTable.moduleName
        val businessName = genTable.businessName
        val packageName = genTable.packageName
        val tplCategory = genTable.tplCategory
        val functionName = genTable.functionName
        val velocityContext = VelocityContext()
        velocityContext.put("tplCategory", genTable.tplCategory)
        velocityContext.put("tableName", genTable.tableName)
        velocityContext.put(
            "functionName",
            if (StringUtils.isNotEmpty(functionName)) functionName else "【请填写功能名称】"
        )
        velocityContext.put("ClassName", genTable.className)
        velocityContext.put("className", StringUtils.uncapitalize(genTable.className))
        velocityContext.put("moduleName", genTable.moduleName)
        velocityContext.put("BusinessName", StringUtils.capitalize(genTable.businessName))
        velocityContext.put("businessName", genTable.businessName)
        velocityContext.put("basePackage", getPackagePrefix(packageName))
        velocityContext.put("packageName", packageName)
        velocityContext.put("author", genTable.functionAuthor)
        velocityContext.put("datetime", getDate())
        velocityContext.put("pkColumn", genTable.pkColumn)
        velocityContext.put("importList", getImportList(genTable))
        velocityContext.put("permissionPrefix", getPermissionPrefix(moduleName, businessName))
        velocityContext.put("columns", genTable.columns)
        velocityContext.put("table", genTable)
        velocityContext.put("dicts", getDicts(genTable))
        setMenuVelocityContext(velocityContext, genTable)
        if (GenConstants.TPL_TREE == tplCategory) {
            setTreeVelocityContext(velocityContext, genTable)
        }
        return velocityContext
    }

    fun setMenuVelocityContext(context: VelocityContext, genTable: GenTable) {
        val options = genTable.options
        val paramsObj = parseMap(options)
        val parentMenuId = getParentMenuId(paramsObj)
        context.put("parentMenuId", parentMenuId)
    }

    fun setTreeVelocityContext(context: VelocityContext, genTable: GenTable) {
        val options = genTable.options
        val paramsObj = parseMap(options)
        val treeCode = getTreecode(paramsObj)
        val treeParentCode = getTreeParentCode(paramsObj)
        val treeName = getTreeName(paramsObj)
        context.put("treeCode", treeCode)
        context.put("treeParentCode", treeParentCode)
        context.put("treeName", treeName)
        context.put("expandColumn", getExpandColumn(genTable))
        if (paramsObj!!.containsKey(GenConstants.TREE_PARENT_CODE)) {
            context.put("tree_parent_code", paramsObj[GenConstants.TREE_PARENT_CODE])
        }
        if (paramsObj.containsKey(GenConstants.TREE_NAME)) {
            context.put("tree_name", paramsObj[GenConstants.TREE_NAME])
        }
    }

    /**
     * 获取模板信息
     *
     * @return 模板列表
     */
    fun getTemplateList(tplCategory: String): MutableList<String> {
        val templates: MutableList<String> = mutableListOf()
        templates.add("vm/java/domain.java.vm")
        templates.add("vm/java/vo.java.vm")
        templates.add("vm/java/bo.java.vm")
        templates.add("vm/java/mapper.java.vm")
        templates.add("vm/java/service.java.vm")
        templates.add("vm/java/serviceImpl.java.vm")
        templates.add("vm/java/controller.java.vm")
        templates.add("vm/xml/mapper.xml.vm")
        if (isOracle()) {
            templates.add("vm/sql/oracle/sql.vm")
        } else if (isPostgerSql()) {
            templates.add("vm/sql/postgres/sql.vm")
        } else if (isSqlServer()) {
            templates.add("vm/sql/sqlserver/sql.vm")
        } else {
            templates.add("vm/sql/sql.vm")
        }
        templates.add("vm/ts/api.ts.vm")
        templates.add("vm/ts/types.ts.vm")
        if (GenConstants.TPL_CRUD == tplCategory) {
            templates.add("vm/vue/index.vue.vm")
        } else if (GenConstants.TPL_TREE == tplCategory) {
            templates.add("vm/vue/index-tree.vue.vm")
        }
        return templates
    }

    /**
     * 获取文件名
     */
    fun getFileName(template: String, genTable: GenTable): String {
        // 文件名称
        var fileName = ""
        // 包路径
        val packageName = genTable.packageName
        // 模块名
        val moduleName = genTable.moduleName
        // 大写类名
        val className = genTable.className
        // 业务名称
        val businessName = genTable.businessName
        val javaPath = PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/")
        val mybatisPath = "$MYBATIS_PATH/$moduleName"
        val vuePath = "vue"
        if (template.contains("domain.java.vm")) {
            fileName = format("{}/domain/{}.java", javaPath, className!!)
        }
        if (template.contains("vo.java.vm")) {
            fileName = format("{}/domain/vo/{}Vo.java", javaPath, className!!)
        }
        if (template.contains("bo.java.vm")) {
            fileName = format("{}/domain/bo/{}Bo.java", javaPath, className!!)
        }
        if (template.contains("mapper.java.vm")) {
            fileName = format("{}/mapper/{}Mapper.java", javaPath, className!!)
        } else if (template.contains("service.java.vm")) {
            fileName = format("{}/service/I{}Service.java", javaPath, className!!)
        } else if (template.contains("serviceImpl.java.vm")) {
            fileName = format("{}/service/impl/{}ServiceImpl.java", javaPath, className!!)
        } else if (template.contains("controller.java.vm")) {
            fileName = format("{}/controller/{}Controller.java", javaPath, className!!)
        } else if (template.contains("mapper.xml.vm")) {
            fileName = format("{}/{}Mapper.xml", mybatisPath, className!!)
        } else if (template.contains("sql.vm")) {
            fileName = businessName + "Menu.sql"
        } else if (template.contains("api.ts.vm")) {
            fileName = format("{}/api/{}/{}/index.ts", vuePath, moduleName!!, businessName!!)
        } else if (template.contains("types.ts.vm")) {
            fileName = format("{}/api/{}/{}/types.ts", vuePath, moduleName!!, businessName!!)
        } else if (template.contains("index.vue.vm")) {
            fileName = format("{}/views/{}/{}/index.vue", vuePath, moduleName!!, businessName!!)
        } else if (template.contains("index-tree.vue.vm")) {
            fileName = format("{}/views/{}/{}/index.vue", vuePath, moduleName!!, businessName!!)
        }
        return fileName
    }

    /**
     * 获取包前缀
     *
     * @param packageName 包名称
     * @return 包前缀名称
     */
    fun getPackagePrefix(packageName: String?): String {
        val lastIndex = packageName!!.lastIndexOf(".")
        return StringUtils.substring(packageName, 0, lastIndex)
    }

    /**
     * 根据列类型获取导入包
     *
     * @param genTable 业务表对象
     * @return 返回需要导入的包列表
     */
    fun getImportList(genTable: GenTable): HashSet<String> {
        val columns: MutableList<GenTableColumn>? = genTable.columns
        val importList = HashSet<String>()
        for (column in columns!!) {
            if (!column.isSuperColumn() && GenConstants.TYPE_DATE == column.javaType) {
                importList.add("java.util.Date")
                importList.add("com.fasterxml.jackson.annotation.JsonFormat")
            } else if (!column.isSuperColumn() && GenConstants.TYPE_BIGDECIMAL == column.javaType) {
                importList.add("java.math.BigDecimal")
            }
        }
        return importList
    }

    /**
     * 根据列类型获取字典组
     *
     * @param genTable 业务表对象
     * @return 返回字典组
     */
    fun getDicts(genTable: GenTable): String {
        val columns: MutableList<GenTableColumn>? = genTable.columns
        val dicts: MutableSet<String> = HashSet()
        addDicts(dicts, columns)
        return StringUtils.join(dicts, ", ")
    }

    /**
     * 添加字典列表
     *
     * @param dicts   字典列表
     * @param columns 列集合
     */
    fun addDicts(dicts: MutableSet<String>, columns: MutableList<GenTableColumn>?) {
        for (column in columns!!) {
            if (!column.isSuperColumn() && StringUtils.isNotEmpty(column.dictType) && StringUtils.equalsAny(
                    column.htmlType,
                    *arrayOf(GenConstants.HTML_SELECT, GenConstants.HTML_RADIO, GenConstants.HTML_CHECKBOX)
                )
            ) {
                dicts.add("'" + column.dictType + "'")
            }
        }
    }

    /**
     * 获取权限前缀
     *
     * @param moduleName   模块名称
     * @param businessName 业务名称
     * @return 返回权限前缀
     */
    fun getPermissionPrefix(moduleName: String?, businessName: String?): String {
        return format("{}:{}", moduleName!!, businessName!!)
    }

    /**
     * 获取上级菜单ID字段
     *
     * @param paramsObj 生成其他选项
     * @return 上级菜单ID字段
     */
    fun getParentMenuId(paramsObj: Dict?): String {
        return if (CollUtil.isNotEmpty(paramsObj) && paramsObj!!.containsKey(GenConstants.PARENT_MENU_ID)
            && StringUtils.isNotEmpty(paramsObj.getStr(GenConstants.PARENT_MENU_ID))
        ) {
            paramsObj.getStr(GenConstants.PARENT_MENU_ID)
        } else DEFAULT_PARENT_MENU_ID
    }

    /**
     * 获取树编码
     *
     * @param paramsObj 生成其他选项
     * @return 树编码
     */
    fun getTreecode(paramsObj: Map<String, Any>?): String {
        return if (CollUtil.isNotEmpty(paramsObj) && paramsObj!!.containsKey(GenConstants.TREE_CODE)) {
            toCamelCase(Convert.toStr(paramsObj[GenConstants.TREE_CODE]))
        } else StringUtils.EMPTY
    }

    /**
     * 获取树父编码
     *
     * @param paramsObj 生成其他选项
     * @return 树父编码
     */
    fun getTreeParentCode(paramsObj: Dict?): String {
        return if (CollUtil.isNotEmpty(paramsObj) && paramsObj!!.containsKey(GenConstants.TREE_PARENT_CODE)) {
            toCamelCase(paramsObj.getStr(GenConstants.TREE_PARENT_CODE))
        } else StringUtils.EMPTY
    }

    /**
     * 获取树名称
     *
     * @param paramsObj 生成其他选项
     * @return 树名称
     */
    fun getTreeName(paramsObj: Dict?): String {
        return if (CollUtil.isNotEmpty(paramsObj) && paramsObj!!.containsKey(GenConstants.TREE_NAME)) {
            toCamelCase(paramsObj.getStr(GenConstants.TREE_NAME))
        } else StringUtils.EMPTY
    }

    /**
     * 获取需要在哪一列上面显示展开按钮
     *
     * @param genTable 业务表对象
     * @return 展开按钮列序号
     */
    fun getExpandColumn(genTable: GenTable): Int {
        val options = genTable.options
        val paramsObj = parseMap(options)
        val treeName = paramsObj!!.getStr(GenConstants.TREE_NAME)
        var num = 0
        for (column in genTable.columns!!) {
            if (column.isList()) {
                num++
                val columnName = column.columnName
                if (columnName == treeName) {
                    break
                }
            }
        }
        return num
    }
}
