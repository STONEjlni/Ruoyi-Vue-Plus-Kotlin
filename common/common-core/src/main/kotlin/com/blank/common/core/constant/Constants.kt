package com.blank.common.core.constant

/**
 * 通用常量信息
 */
interface Constants {
    companion object {
        /**
         * UTF-8 字符集
         */
        const val UTF8 = "UTF-8"

        /**
         * GBK 字符集
         */
        const val GBK = "GBK"

        /**
         * www主域
         */
        const val WWW = "www."

        /**
         * http请求
         */
        const val HTTP = "http://"

        /**
         * https请求
         */
        const val HTTPS = "https://"

        /**
         * 通用成功标识
         */
        const val SUCCESS = "0"

        /**
         * 通用失败标识
         */
        const val FAIL = "1"

        /**
         * 登录成功
         */
        const val LOGIN_SUCCESS = "Success"

        /**
         * 注销
         */
        const val LOGOUT = "Logout"

        /**
         * 注册
         */
        const val REGISTER = "Register"

        /**
         * 登录失败
         */
        const val LOGIN_FAIL = "Error"

        /**
         * 验证码有效期（分钟）
         */
        const val CAPTCHA_EXPIRATION = 2

        /**
         * 令牌
         */
        const val TOKEN = "token"

        /**
         * 顶级部门id
         */
        const val TOP_PARENT_ID = 0L
    }
}
