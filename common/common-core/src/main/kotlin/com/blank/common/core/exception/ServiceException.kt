package com.blank.common.core.exception

import java.io.Serial

/**
 * 业务异常
 */
class ServiceException : RuntimeException {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 错误码
     */
    var code: Int? = null

    /**
     * 错误明细，内部调试错误
     */
    var detailMessage: String? = null

    /**
     * 错误提示
     */
    override var message: String? = null

    constructor(message: String?) : this(message, null, null) {
    }

    constructor(message: String?, code: Int?) : this(message, code, null) {
    }

    constructor(message: String?, code: Int?, detailMessage: String?) : super(message) {
        this.code = code
        this.message = message
        this.detailMessage = detailMessage
    }

    fun setMessage(message: String?): ServiceException {
        this.message = message
        return this
    }

    fun setDetailMessage(detailMessage: String?): ServiceException {
        this.detailMessage = detailMessage
        return this
    }
}
