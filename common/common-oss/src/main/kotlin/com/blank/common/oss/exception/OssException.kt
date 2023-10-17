package com.blank.common.oss.exception

import java.io.Serial
import java.io.Serializable

/**
 * OSS异常类
 */
class OssException(msg: String) : RuntimeException(msg), Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
