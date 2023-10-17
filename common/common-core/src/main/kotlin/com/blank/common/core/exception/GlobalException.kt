package com.blank.common.core.exception

import java.io.Serial

/**
 * 全局异常
 */
class GlobalException : RuntimeException {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 错误提示
     */
    override var message: String? = null

    /**
     * 错误明细，内部调试错误
     */
    var detailMessage: String? = null

    constructor(message: String?) : super(message) {
        this.message = message
    }

    fun setDetailMessage(detailMessage: String?): GlobalException {
        this.detailMessage = detailMessage
        return this
    }

    fun setMessage(message: String?): GlobalException {
        this.message = message
        return this
    }
}
