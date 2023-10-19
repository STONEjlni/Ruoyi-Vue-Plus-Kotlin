package com.blank.common.mybatis.helper

import cn.hutool.core.convert.Convert
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.exception.ServiceException
import com.blank.common.mybatis.enums.DataBaseType
import com.blank.common.mybatis.enums.DataBaseType.Companion.find
import java.sql.SQLException
import javax.sql.DataSource

/**
 * 数据库助手
 */
object DataBaseHelper {
    private val DATA_SOURCE = SpringUtil.getBean(DataSource::class.java)

    /**
     * 获取当前数据库类型
     */
    fun getDataBaseType(): DataBaseType? {
        try {
            DATA_SOURCE.connection.use { conn ->
                val metaData = conn.metaData
                val databaseProductName = metaData.databaseProductName
                return find(databaseProductName)
            }
        } catch (e: SQLException) {
            throw ServiceException(e.message)
        }
    }

    fun isMySql(): Boolean {
        return DataBaseType.MY_SQL === getDataBaseType()
    }

    fun isOracle(): Boolean {
        return DataBaseType.ORACLE === getDataBaseType()
    }

    fun isPostgerSql(): Boolean {
        return DataBaseType.POSTGRE_SQL === getDataBaseType()
    }

    fun isSqlServer(): Boolean {
        return DataBaseType.SQL_SERVER === getDataBaseType()
    }

    fun findInSet(var1: Any, var2: String): String {
        val dataBasyType = getDataBaseType()
        val `var` = Convert.toStr(var1)
        if (dataBasyType === DataBaseType.SQL_SERVER) {
            // charindex(',100,' , ',0,100,101,') <> 0
            return "charindex(',${`var`},' , ','+${var2}+',') <> 0"
        } else if (dataBasyType === DataBaseType.POSTGRE_SQL) {
            // (select position(',100,' in ',0,100,101,')) <> 0
            return "(select position(',${`var`},' in ','||${var2}||',')) <> 0"
        } else if (dataBasyType === DataBaseType.ORACLE) {
            // instr(',0,100,101,' , ',100,') <> 0
            return "instr(','||${var2}||',' , ',${`var`},') <> 0"
        }
        // find_in_set(100 , '0,100,101')
        return "find_in_set('${`var`}' , ${var2}) <> 0"
    }

    /**
     * 获取当前加载的数据库名
     */
    fun getDataSourceNameList(): MutableList<String> {
        return mutableListOf("master")
    }
}
