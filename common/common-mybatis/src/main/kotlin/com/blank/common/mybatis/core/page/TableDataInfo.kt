package com.blank.common.mybatis.core.page

import cn.hutool.http.HttpStatus
import com.mybatisflex.core.paginate.Page
import java.io.Serial
import java.io.Serializable

/**
 * 表格分页数据对象
 */
class TableDataInfo<T> @JvmOverloads constructor(
    /**
     * 分页
     *
     * @param list  列表数据
     * @param total 总记录数
     */
    list: List<T>? = null,
    /**
     * 总记录数
     */
    var total: Long? = null
) : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L

        fun <T> build(page: Page<T>): TableDataInfo<T> {
            val rspData = TableDataInfo<T>()
            rspData.code = HttpStatus.HTTP_OK
            rspData.msg = "查询成功"
            rspData.rows = page.records
            rspData.total = page.totalPage
            return rspData
        }

        fun <T> build(list: List<T>): TableDataInfo<T> {
            val rspData = TableDataInfo<T>()
            rspData.code = HttpStatus.HTTP_OK
            rspData.msg = "查询成功"
            rspData.rows = list
            rspData.total = list.size.toLong()
            return rspData
        }

        fun <T> build(): TableDataInfo<T> {
            val rspData = TableDataInfo<T>()
            rspData.code = HttpStatus.HTTP_OK
            rspData.msg = "查询成功"
            return rspData
        }
    }

    /**
     * 列表数据
     */
    var rows: List<T>? = list

    /**
     * 消息状态码
     */
    var code = 0

    /**
     * 消息内容
     */
    var msg: String? = null
}
