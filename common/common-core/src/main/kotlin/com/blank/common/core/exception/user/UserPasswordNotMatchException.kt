package com.blank.common.core.exception.user

import java.io.Serial

/**
 * 用户密码不正确或不符合规范异常类
 */
class UserPasswordNotMatchException : UserException("user.password.not.match") {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
