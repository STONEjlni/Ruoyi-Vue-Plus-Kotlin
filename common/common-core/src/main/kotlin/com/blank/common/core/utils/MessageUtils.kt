package com.blank.common.core.utils

import cn.hutool.extra.spring.SpringUtil
import org.springframework.context.MessageSource
import org.springframework.context.NoSuchMessageException
import org.springframework.context.i18n.LocaleContextHolder

/**
 * 获取i18n资源文件
 */
object MessageUtils {
    private val MESSAGE_SOURCE = SpringUtil.getBean(
        MessageSource::class.java
    )

    /**
     * 根据消息键和参数 获取消息 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return 获取国际化翻译值
     */
    fun message(code: String, vararg args: Any?): String {
        return try {
            MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale())
        } catch (e: NoSuchMessageException) {
            code
        }
    }
}
