package com.blank.common.mybatis.enums

import cn.hutool.core.util.StrUtil

/**
 * 数据库类型
 */
enum class DataBaseType(
    val type: String
) {
    /**
     * MySQL
     */
    MY_SQL("MySQL"),

    /**
     * Oracle
     */
    ORACLE("Oracle"),

    /**
     * PostgreSQL
     */
    POSTGRE_SQL("PostgreSQL"),

    /**
     * SQL Server
     */
    SQL_SERVER("Microsoft SQL Server");

    companion object {
        fun find(databaseProductName: String?): DataBaseType? {
            if (StrUtil.isBlank(databaseProductName)) {
                return null
            }
            for (type in entries) {
                if (type.type == databaseProductName) {
                    return type
                }
            }
            return null
        }
    }
}
