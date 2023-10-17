package com.blank.common.core.exception

import java.io.Serial

/**
 * 工具类异常
 */
class UtilException : RuntimeException {
    companion object {
        @Serial
        private const val serialVersionUID = 8247610319171014183L
    }

    constructor(e: Throwable) : super(e.message, e) {
    }

    constructor(message: String?) : super(message) {
    }

    constructor(message: String?, throwable: Throwable?) : super(message, throwable) {
    }
}
