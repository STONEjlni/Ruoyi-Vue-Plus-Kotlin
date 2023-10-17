package com.blank.common.core.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 线程池 配置属性
 */
@ConfigurationProperties(prefix = "thread-pool")
class ThreadPoolProperties {
    /**
     * 是否开启线程池
     */
    val enabled: Boolean = false

    /**
     * 队列最大长度
     */
    val queueCapacity: Int = 0

    /**
     * 线程池维护线程所允许的空闲时间
     */
    val keepAliveSeconds: Int = 0
}

