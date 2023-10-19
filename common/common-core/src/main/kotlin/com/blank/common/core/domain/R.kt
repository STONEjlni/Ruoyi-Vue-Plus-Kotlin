package com.blank.common.core.domain

import com.blank.common.core.constant.HttpStatus
import java.io.Serial
import java.io.Serializable

/**
 * 响应信息主体
 */
class R<T> : Serializable {
    var code: Int? = 0
    var msg: String? = null
    var data: T? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L

        /**
         * 成功
         */
        const val SUCCESS = 200

        /**
         * 失败
         */
        const val FAIL = 500

        /**
         * 返回成功消息
         *
         * @param code 编码
         * @param msg 返回内容
         * @param data 数据对象
         * @return 成功消息
         */
        @JvmOverloads
        fun <T> ok(code: Int? = SUCCESS, msg: String? = "操作成功", data: T? = null): R<T> {
            return restResult(data, code, msg)
        }

        /**
         * 返回失败消息
         *
         * @param code 编码
         * @param msg 返回内容
         * @param data 数据对象
         * @return 失败消息
         */
        @JvmOverloads
        fun <T> fail(code: Int? = FAIL, msg: String? = "操作失败", data: T? = null): R<T> {
            return restResult(data, code, msg)
        }

        /**
         * 返回警告消息
         *
         * @param code 编码
         * @param msg 返回内容
         * @param data 数据对象
         * @return 警告消息
         */
        @JvmOverloads
        fun <T> warn(code: Int? = HttpStatus.WARN, msg: String? = "操作失败", data: T? = null): R<T> {
            return restResult(data, code, msg)
        }

        private fun <T> restResult(data: T?, code: Int?, msg: String?): R<T> {
            val r: R<T> = R()
            r.code = code
            r.data = data
            r.msg = msg
            return r
        }

        fun <T> isError(ret: R<T>): Boolean {
            return !isSuccess(ret)
        }

        fun <T> isSuccess(ret: R<T>): Boolean {
            return SUCCESS == ret.code
        }
    }
}


