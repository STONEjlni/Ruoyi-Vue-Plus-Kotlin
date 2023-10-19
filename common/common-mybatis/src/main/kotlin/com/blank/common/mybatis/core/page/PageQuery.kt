package com.blank.common.mybatis.core.page

import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.sql.SqlUtil
import com.mybatisflex.core.constant.SqlConsts
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryColumn
import com.mybatisflex.core.query.QueryOrderBy
import org.apache.commons.lang3.StringUtils
import java.io.Serial
import java.io.Serializable


/**
 * 分页查询实体类
 */
class PageQuery : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 分页大小
     */
    var pageSize: Int? = null

    /**
     * 当前页数
     */
    var pageNum: Int? = null

    /**
     * 排序列
     */
    var orderByColumn: String? = null

    /**
     * 排序的方向desc或者asc
     */
    var isAsc: String? = null

    /**
     * 当前记录起始索引 默认值
     */
    var DEFAULT_PAGE_NUM = 1

    /**
     * 每页显示记录数 默认值 默认查全部
     */
    var DEFAULT_PAGE_SIZE = Int.MAX_VALUE

    fun <T> build(): Page<T> {
        var pageNum: Int =
            ObjectUtil.defaultIfNull(pageNum, DEFAULT_PAGE_NUM)
        val pageSize: Int =
            ObjectUtil.defaultIfNull(pageSize, DEFAULT_PAGE_SIZE)
        if (pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM
        }
        return Page(pageNum, pageSize)
    }

    /**
     * 构建排序
     *
     *
     * 支持的用法如下:
     * {isAsc:"asc",orderByColumn:"id"} order by id asc
     * {isAsc:"asc",orderByColumn:"id,createTime"} order by id asc,create_time asc
     * {isAsc:"desc",orderByColumn:"id,createTime"} order by id desc,create_time desc
     * {isAsc:"asc,desc",orderByColumn:"id,createTime"} order by id asc,create_time desc
     */
    fun buildOrderBy(): Array<QueryOrderBy> {
        if (StrUtil.isBlank(orderByColumn) || StrUtil.isBlank(isAsc)) {
            return arrayOf()
        }
        var orderBy: String = SqlUtil.escapeOrderBySql(orderByColumn!!)
        orderBy = StringUtilsExtend.toUnderScoreCase(orderBy)

        // 兼容前端排序类型
        isAsc =
            StringUtils.replaceEach(isAsc, arrayOf("ascending", "descending"), arrayOf("asc", "desc"))
        val orderByArr: Array<String> = orderBy.split(StringUtilsExtend.SEPARATOR)
            .toTypedArray()
        val isAscArr: Array<String> = isAsc!!.split(StringUtilsExtend.SEPARATOR)
            .toTypedArray()
        if (isAscArr.size != 1 && isAscArr.size != orderByArr.size) {
            throw ServiceException("排序参数有误")
        }
        val orderBys = arrayOf<QueryOrderBy>() // orderByArr.size
        // 每个字段各自排序
        for (i in orderByArr.indices) {
            val orderByStr = orderByArr[i]
            val isAscStr = if (isAscArr.size == 1) isAscArr[0] else isAscArr[i]
            when (isAscStr) {
                "asc" -> {
                    orderBys[i] = QueryOrderBy(QueryColumn(orderByStr), SqlConsts.ASC)
                }
                "desc" -> {
                    orderBys[i] = QueryOrderBy(QueryColumn(orderByStr), SqlConsts.DESC)
                }
                else -> {
                    throw ServiceException("排序参数有误")
                }
            }

            /*if ("asc" == isAscStr) {
                orderBys[i] = QueryOrderBy(QueryColumn(orderByStr), SqlConsts.ASC)
            } else if ("desc" == isAscStr) {
                orderBys[i] = QueryOrderBy(QueryColumn(orderByStr), SqlConsts.DESC)
            } else {
                throw ServiceException("排序参数有误")
            }*/
        }
        return orderBys
    }
}
