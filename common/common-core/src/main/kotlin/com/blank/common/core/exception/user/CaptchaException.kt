package com.blank.common.core.exception.user

import java.io.Serial

/**
 * 验证码错误异常类
 */
class CaptchaException : UserException(
    "user.jcaptcha.error"
) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
