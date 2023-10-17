package com.blank.common.core.exception

import java.io.Serial

/**
 * 演示模式异常
 */
class DemoModeException : RuntimeException() {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
