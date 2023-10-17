package com.blank.monitor.admin.config

import de.codecentric.boot.admin.server.config.EnableAdminServer
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
import org.springframework.boot.task.TaskExecutorBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Lazy
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor
import java.util.concurrent.Executor


/**
 * springboot-admin server配置类
 */
@Configuration
@EnableAdminServer
class AdminServerConfig {
    @Lazy
    @Bean(name = [TaskExecutionAutoConfiguration.APPLICATION_TASK_EXECUTOR_BEAN_NAME])
    @ConditionalOnMissingBean(
        Executor::class
    )
    fun applicationTaskExecutor(builder: TaskExecutorBuilder): ThreadPoolTaskExecutor {
        return builder.build()
    }
}

