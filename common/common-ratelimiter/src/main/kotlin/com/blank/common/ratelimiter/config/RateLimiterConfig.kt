package com.blank.common.ratelimiter.config

import com.blank.common.ratelimiter.aspectj.RateLimiterAspect
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConfiguration

/**
 * 限流配置
 */
@AutoConfiguration(after = [RedisConfiguration::class])
class RateLimiterConfig {
    @Bean
    fun rateLimiterAspect(): RateLimiterAspect {
        return RateLimiterAspect()
    }
}
