package com.blank.common.core.exception.user

import java.io.Serial

/**
 * 验证码失效异常类
 */
class CaptchaExpireException : UserException(
    "user.jcaptcha.expire"
) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
