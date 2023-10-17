package com.blank.common.core.config

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.config.properties.ThreadPoolProperties
import com.blank.common.core.utils.Threads
import jakarta.annotation.PreDestroy
import org.apache.commons.lang3.concurrent.BasicThreadFactory
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.ThreadPoolExecutor

/**
 * 线程池配置
 **/
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(ThreadPoolProperties::class)
class ThreadPoolConfig {

    /**
     * 核心线程数 = cpu 核心数 + 1
     */
    private val core = Runtime.getRuntime().availableProcessors() + 1

    private var scheduledExecutorService: ScheduledExecutorService? = null

    @Bean(name = ["threadPoolTaskExecutor"])
    @ConditionalOnProperty(prefix = "thread-pool", name = ["enabled"], havingValue = "true")
    fun threadPoolTaskExecutor(threadPoolProperties: ThreadPoolProperties): ThreadPoolTaskExecutor {
        val executor = ThreadPoolTaskExecutor()
        executor.corePoolSize = core
        executor.maxPoolSize = core * 2
        executor.queueCapacity = threadPoolProperties.queueCapacity
        executor.keepAliveSeconds = threadPoolProperties.keepAliveSeconds
        executor.setRejectedExecutionHandler(ThreadPoolExecutor.CallerRunsPolicy())
        return executor
    }

    /**
     * 执行周期性或定时任务
     */
    @Bean(name = ["scheduledExecutorService"])
    protected fun scheduledExecutorService(): ScheduledExecutorService {
        val scheduledThreadPoolExecutor: ScheduledThreadPoolExecutor = object : ScheduledThreadPoolExecutor(
            core,
            BasicThreadFactory.Builder().namingPattern("schedule-pool-%d").daemon(true).build(),
            CallerRunsPolicy()
        ) {
            override fun afterExecute(r: Runnable, t: Throwable) {
                super.afterExecute(r, t)
                Threads.printException(r, t)
            }
        }
        scheduledExecutorService = scheduledThreadPoolExecutor
        return scheduledThreadPoolExecutor
    }

    /**
     * 销毁事件
     */
    @PreDestroy
    fun destroy() {
        try {
            log.info { "====关闭后台任务任务线程池====" }
            Threads.shutdownAndAwaitTermination(scheduledExecutorService)
        } catch (e: Exception) {
            log.error(e) { "${e.message}" }
        }
    }
}
