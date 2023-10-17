package com.blank.common.mail.utils

import jakarta.mail.Authenticator
import jakarta.mail.PasswordAuthentication

/**
 * 用户名密码验证器
 */
class UserPassAuthenticator(
    /**
     * 构造
     *
     * @param user 用户名
     * @param pass 密码
     */
    private val user: String,
    private val pass: String
) : Authenticator() {
    override fun getPasswordAuthentication(): PasswordAuthentication {
        return PasswordAuthentication(user, pass)
    }
}
