package com.blank.generator.mapper

import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.generator.domain.GenTableColumn
import org.apache.ibatis.annotations.Param

/**
 * 代码生成字段 数据层
 */
interface GenTableColumnMapper : BaseMapperPlus<GenTableColumn, GenTableColumn> {
    /**
     * 根据表名称查询列信息
     *
     * @param tableName 表名称
     * @param dataName  数据源名称
     * @return 列信息
     */
    fun selectDbTableColumnsByName(
        @Param("tableName") tableName: String?,
        dataName: String?
    ): MutableList<GenTableColumn>?
}
