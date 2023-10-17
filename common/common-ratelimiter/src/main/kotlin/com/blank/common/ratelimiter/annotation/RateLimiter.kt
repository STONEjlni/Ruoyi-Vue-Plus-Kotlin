package com.blank.common.ratelimiter.annotation

import com.blank.common.ratelimiter.enums.LimitType

/**
 * 限流注解
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class RateLimiter(
    /**
     * 限流key,支持使用Spring el表达式来动态获取方法上的参数值
     * 格式类似于  #code.id #{#code}
     */
    val key: String = "",

    /**
     * 限流时间,单位秒
     */
    val time: Int = 60,

    /**
     * 限流次数
     */
    val count: Int = 100,

    /**
     * 限流类型
     */
    val limitType: LimitType = LimitType.DEFAULT,

    /**
     * 提示消息 支持国际化 格式为 {code}
     */
    val message: String = "{rate.limiter.message}"
)
