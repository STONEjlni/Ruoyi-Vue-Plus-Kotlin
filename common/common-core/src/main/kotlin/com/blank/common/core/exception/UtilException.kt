package com.blank.common.core.exception

import java.io.Serial

/**
 * 工具类异常
 */
class UtilException @JvmOverloads constructor(message: String? = null, throwable: Throwable? = null) :
    RuntimeException(message, throwable) {
    companion object {
        @Serial
        private const val serialVersionUID = 8247610319171014183L
    }

}
