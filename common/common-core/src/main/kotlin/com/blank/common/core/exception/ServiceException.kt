package com.blank.common.core.exception

import java.io.Serial

/**
 * 业务异常
 */
class ServiceException @JvmOverloads constructor(
    /**
     * 错误提示
     */
    override var message: String? = null,
    /**
     * 错误码
     */
    var code: Int? = null,
    /**
     * 错误明细，内部调试错误
     */
    var detailMessage: String? = null
) : RuntimeException(message) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
