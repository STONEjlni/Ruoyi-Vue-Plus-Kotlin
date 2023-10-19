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
    var enabled: Boolean? = null

    /**
     * SMTP服务器域名
     */
    var host: String? = null

    /**
     * SMTP服务端口
     */
    var port: Int? = null

    /**
     * 是否需要用户名密码验证
     */
    var auth: Boolean = false

    /**
     * 用户名
     */
    var user: String? = null

    /**
     * 密码
     */
    var pass: String? = null

    /**
     * 发送方，遵循RFC-822标准
     */
    var from: String? = null

    /**
     * 使用 STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展。它将纯文本连接升级为加密连接（TLS或SSL）， 而不是使用一个单独的加密通信端口。
     */
    var starttlsEnable: Boolean = false

    /**
     * 使用 SSL安全连接
     */
    var sslEnable: Boolean = false

    /**
     * SMTP超时时长，单位毫秒，缺省值不超时
     */
    var timeout: Long? = null

    /**
     * Socket连接超时值，单位毫秒，缺省值不超时
     */
    var connectionTimeout: Long? = null
}
