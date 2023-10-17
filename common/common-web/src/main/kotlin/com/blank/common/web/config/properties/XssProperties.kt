package com.blank.common.web.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * xss过滤 配置属性
 */
@ConfigurationProperties(prefix = "xss")
class XssProperties {
    /**
     * 过滤开关
     */
    val enabled: String? = null

    /**
     * 排除链接（多个用逗号分隔）
     */
    val excludes: String? = null

    /**
     * 匹配链接
     */
    val urlPatterns: String? = null
}
