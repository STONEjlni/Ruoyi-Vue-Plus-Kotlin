package com.blank.common.core.exception.user

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.core.exception.base.BaseException
import java.io.Serial

/**
 * 用户信息异常类
 */
@Open
@NoArg
class UserException @JvmOverloads constructor(
    code: String? = null,
    defaultMessage: String? = null,
    vararg args: Any?
) : BaseException(
    module = "user",
    code = code,
    args = args,
    defaultMessage = defaultMessage
) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
