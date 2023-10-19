package com.blank.common.web.core

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.lang.Nullable
import org.springframework.web.servlet.LocaleResolver
import java.util.*

/**
 * 获取请求头国际化信息
 */
class I18nLocaleResolver : LocaleResolver {
    override fun resolveLocale(httpServletRequest: HttpServletRequest): Locale {
        val language = httpServletRequest.getHeader("content-language")
        var locale = Locale.getDefault()
        if (language != null && language.isNotEmpty()) {
            val split = language.split("_").toTypedArray()
            locale = Locale(split[0], split[1])
        }
        return locale
    }

    override fun setLocale(
        httpServletRequest: HttpServletRequest,
        @Nullable response: HttpServletResponse?,
        @Nullable locale: Locale?
    ) {
    }
}
