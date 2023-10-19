package com.blank.common.core.exception

import java.io.Serial

/**
 * 全局异常
 */
class GlobalException(
    /**
     * 错误提示
     */
    override var message: String?
) : RuntimeException(message) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 错误明细，内部调试错误
     */
    var detailMessage: String? = null
}
