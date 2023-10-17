package com.blank.common.core.config

import cn.hutool.core.util.ArrayUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.exception.ServiceException
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.scheduling.annotation.AsyncConfigurer
import org.springframework.scheduling.annotation.EnableAsync
import java.lang.reflect.Method
import java.util.concurrent.Executor

/**
 * 异步配置
 */
@EnableAsync(proxyTargetClass = true)
@AutoConfiguration
class AsyncConfig : AsyncConfigurer {

    /**
     * 自定义 @Async 注解使用系统线程池
     */
    override fun getAsyncExecutor(): Executor {
        return SpringUtil.getBean("scheduledExecutorService")
    }

    /**
     * 异步执行异常处理
     */
    override fun getAsyncUncaughtExceptionHandler(): AsyncUncaughtExceptionHandler {
        return AsyncUncaughtExceptionHandler { throwable: Throwable, method: Method, objects: Array<Any> ->
            throwable.printStackTrace()
            val sb = StringBuilder()
            sb.append("Exception message - ").append(throwable.message)
                .append(", Method name - ").append(method.name)
            if (ArrayUtil.isNotEmpty(objects)) {
                sb.append(", Parameter value - ").append(objects.contentToString())
            }
            throw ServiceException(sb.toString())
        }
    }
}
