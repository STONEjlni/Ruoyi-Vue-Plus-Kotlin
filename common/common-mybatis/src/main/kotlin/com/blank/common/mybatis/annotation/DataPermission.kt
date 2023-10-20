package com.blank.common.mybatis.annotation

import cn.hutool.extra.spring.SpringUtil
import com.blank.common.mybatis.handler.PlusDataPermissionHandler
import com.mybatisflex.core.query.QueryWrapper

/**
 * 数据权限组
 */
class DataPermission(val value: Array<DataColumn>) {
    companion object {
        private val DATA_PERMISSION_HANDLER: PlusDataPermissionHandler =
            SpringUtil.getBean(PlusDataPermissionHandler::class.java)

        fun of(vararg value: DataColumn): DataPermission {
            return DataPermission(arrayOf(*value))
        }
    }

    fun handler(queryWrapper: QueryWrapper) {
        DATA_PERMISSION_HANDLER.handlerDataPermission(this, queryWrapper, true)
    }

    fun toSQL(isSelect: Boolean): String {
        return DATA_PERMISSION_HANDLER.getSQL(this, isSelect)
    }

    fun toSQL(): String {
        return toSQL(true)
    }
}


