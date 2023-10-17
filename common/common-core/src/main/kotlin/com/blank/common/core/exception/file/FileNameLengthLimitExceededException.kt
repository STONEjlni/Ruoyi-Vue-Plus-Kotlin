package com.blank.common.core.exception.file

import java.io.Serial

/**
 * 文件名称超长限制异常类
 */
class FileNameLengthLimitExceededException(
    defaultFileNameLength: Int
) : FileException(
    "upload.filename.exceed.length", arrayOf(defaultFileNameLength)
) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
