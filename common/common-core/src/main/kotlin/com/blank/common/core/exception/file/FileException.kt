package com.blank.common.core.exception.file

import com.blank.common.core.annotation.Open
import com.blank.common.core.exception.base.BaseException
import java.io.Serial

/**
 * 文件信息异常类
 */
@Open
class FileException(
    code: String, args: Array<Any>
) : BaseException(
    module = "file", code = code, args = args, defaultMessage = null
) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

}
