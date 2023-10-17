package com.blank.common.social.config

import com.blank.common.social.config.properties.SocialProperties
import com.blank.common.social.utils.AuthRedisStateCache
import me.zhyd.oauth.cache.AuthStateCache
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * Social 配置属性
 */
@AutoConfiguration
@EnableConfigurationProperties(SocialProperties::class)
class SocialAutoConfiguration {
    @Bean
    fun authStateCache(): AuthStateCache {
        return AuthRedisStateCache()
    }
}
