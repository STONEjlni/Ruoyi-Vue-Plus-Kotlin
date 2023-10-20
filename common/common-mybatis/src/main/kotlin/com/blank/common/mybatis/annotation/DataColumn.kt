package com.blank.common.mybatis.annotation

import com.mybatisflex.core.query.QueryColumn


/**
 * 数据权限
 * <p>
 * 一个注解只能对应一个模板
 *
 */
class DataColumn(
    /**
     * 占位符关键字
     */
    val key: Array<String>,
    /**
     * 占位符替换值
     */
    val value: Array<String>
) {
    companion object {
        fun of(key: Array<String>, value: Array<String>): DataColumn {
            return DataColumn(key, value)
        }

        fun of(key: String, value: String): DataColumn {
            return DataColumn(arrayOf(key), arrayOf(value))
        }

        fun of(key: String, value: QueryColumn): DataColumn {
            return DataColumn(arrayOf(key), arrayOf(value.name))
        }
    }
}

