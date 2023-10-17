package com.blank.generator.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.generator.domain.GenTable
import com.blank.generator.domain.GenTableColumn

/**
 * 代码生成 服务层
 */
interface IGenTableService {
    /**
     * 查询业务字段列表
     *
     * @param tableId 业务字段编号
     * @return 业务字段集合
     */
    fun selectGenTableColumnListByTableId(tableId: Long): MutableList<GenTableColumn>?

    /**
     * 查询业务列表
     *
     * @param genTable 业务信息
     * @return 业务集合
     */
    fun selectPageGenTableList(genTable: GenTable, pageQuery: PageQuery): TableDataInfo<GenTable>?

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    fun selectPageDbTableList(genTable: GenTable, pageQuery: PageQuery): TableDataInfo<GenTable>?

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @param dataName   数据源名称
     * @return 数据库表集合
     */
    fun selectDbTableListByNames(tableNames: Array<String>, dataName: String): MutableList<GenTable>?

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    fun selectGenTableAll(): MutableList<GenTable>?

    /**
     * 查询业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    fun selectGenTableById(id: Long): GenTable?

    /**
     * 修改业务
     *
     * @param genTable 业务信息
     */
    fun updateGenTable(genTable: GenTable)

    /**
     * 删除业务信息
     *
     * @param tableIds 需要删除的表数据ID
     */
    fun deleteGenTableByIds(tableIds: Array<Long>)

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     * @param dataName  数据源名称
     */
    fun importGenTable(tableList: MutableList<GenTable>, dataName: String)

    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    fun previewCode(tableId: Long): Map<String, String>?

    /**
     * 生成代码（下载方式）
     *
     * @param tableId 表名称
     * @return 数据
     */
    fun downloadCode(tableId: Long): ByteArray?

    /**
     * 生成代码（自定义路径）
     *
     * @param tableId 表名称
     */
    fun generatorCode(tableId: Long)

    /**
     * 同步数据库
     *
     * @param tableId 表名称
     */
    fun synchDb(tableId: Long)

    /**
     * 批量生成代码（下载方式）
     *
     * @param tableIds 表ID数组
     * @return 数据
     */
    fun downloadCode(tableIds: Array<String>): ByteArray?

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    fun validateEdit(genTable: GenTable)
}
