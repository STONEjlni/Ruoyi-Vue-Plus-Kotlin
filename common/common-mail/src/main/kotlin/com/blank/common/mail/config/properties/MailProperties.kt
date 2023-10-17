package com.blank.common.mail.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * JavaMail 配置属性
 */
@ConfigurationProperties(prefix = "mail")
class MailProperties {
    /**
     * 过滤开关
     */
    val enabled: Boolean? = null

    /**
     * SMTP服务器域名
     */
    val host: String? = null

    /**
     * SMTP服务端口
     */
    val port: Int? = null

    /**
     * 是否需要用户名密码验证
     */
    val auth: Boolean = false

    /**
     * 用户名
     */
    val user: String? = null

    /**
     * 密码
     */
    val pass: String? = null

    /**
     * 发送方，遵循RFC-822标准
     */
    val from: String? = null

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     */
    val starttlsEnable: Boolean = false

    /**
     * 使用 SSL安全连接
     */
    val sslEnable: Boolean = false

    /**
     * SMTP超时时长，单位毫秒，缺省值不超时
     */
    val timeout: Long? = null

    /**
     * Socket连接超时值，单位毫秒，缺省值不超时
     */
    val connectionTimeout: Long? = null
}
