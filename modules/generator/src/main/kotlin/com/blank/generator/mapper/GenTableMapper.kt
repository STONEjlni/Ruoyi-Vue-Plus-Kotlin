package com.blank.generator.mapper

import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.generator.domain.GenTable
import com.mybatisflex.core.paginate.Page
import org.apache.ibatis.annotations.Param

/**
 * 代码生成 数据层
 */
interface GenTableMapper : BaseMapperPlus<GenTable, GenTable> {
    /**
     * 查询据库列表
     *
     * @param genTable 查询条件
     * @return 数据库表集合
     */
    fun selectPageDbTableList(
        @Param("page") page: Page<GenTable>,
        @Param("genTable") genTable: GenTable
    ): Page<GenTable>

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    fun selectDbTableListByNames(tableNames: Array<String>): MutableList<GenTable>

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    fun selectGenTableAll(): MutableList<GenTable>

    /**
     * 查询表ID业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    fun selectGenTableById(id: Long): GenTable

    /**
     * 查询表名称业务信息
     *
     * @param tableName 表名称
     * @return 业务信息
     */
    fun selectGenTableByName(tableName: String): GenTable

    fun selectTableNameList(dataName: String): MutableList<String>
}
