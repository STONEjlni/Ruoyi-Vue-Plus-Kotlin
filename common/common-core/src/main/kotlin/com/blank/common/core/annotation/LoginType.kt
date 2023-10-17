package com.blank.common.core.annotation

/**
 * 登录类型
 */
enum class LoginType(
    /**
     * 登录重试超出限制提示
     */
    val retryLimitExceed: String,
    /**
     * 登录重试限制计数提示
     */
    val retryLimitCount: String
) {
    /**
     * 密码登录
     */
    PASSWORD("user.password.retry.limit.exceed", "user.password.retry.limit.count"),

    /**
     * 短信登录
     */
    SMS("sms.code.retry.limit.exceed", "sms.code.retry.limit.count"),

    /**
     * 邮箱登录
     */
    EMAIL("email.code.retry.limit.exceed", "email.code.retry.limit.count"),

    /**
     * 小程序登录
     */
    XCX("", "");
}
