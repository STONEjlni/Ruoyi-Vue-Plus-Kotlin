package com.blank.common.idempotent.config

import com.blank.common.idempotent.aspectj.RepeatSubmitAspect
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.data.redis.connection.RedisConfiguration

/**
 * 幂等功能配置
 */
@AutoConfiguration(after = [RedisConfiguration::class])
class IdempotentConfig {
    @Bean
    fun repeatSubmitAspect() = RepeatSubmitAspect()
}
