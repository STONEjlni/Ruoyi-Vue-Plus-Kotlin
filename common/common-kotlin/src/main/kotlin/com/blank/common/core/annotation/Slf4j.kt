package com.blank.common.core.annotation

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

/**
 * 日志
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class Slf4j {
    companion object {
        val <reified T> T.log: KLogger
            inline get() = KotlinLogging.logger { T::class.java.name }
    }
}
