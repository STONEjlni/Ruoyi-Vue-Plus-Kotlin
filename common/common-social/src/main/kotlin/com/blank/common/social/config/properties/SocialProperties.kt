package com.blank.common.social.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * Social 配置属性
 */
@Component
@ConfigurationProperties(prefix = "justauth")
class SocialProperties {
    /**
     * 是否启用
     */
    var enabled: Boolean? = null

    /**
     * 授权类型
     */
    var type: Map<String, SocialLoginConfigProperties>? = null
}
