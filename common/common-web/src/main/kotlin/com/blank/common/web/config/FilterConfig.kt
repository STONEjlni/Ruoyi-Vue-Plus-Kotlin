package com.blank.common.web.config

import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.web.config.properties.XssProperties
import com.blank.common.web.filter.RepeatableFilter
import com.blank.common.web.filter.XssFilter
import jakarta.servlet.DispatcherType
import jakarta.servlet.Filter
import org.apache.commons.lang3.StringUtils
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

/**
 * Filter配置
 */
@AutoConfiguration
@EnableConfigurationProperties(XssProperties::class)
class FilterConfig {
    @Bean
    @ConditionalOnProperty(value = ["xss.enabled"], havingValue = "true")
    fun xssFilterRegistration(xssProperties: XssProperties): FilterRegistrationBean<out Filter> {
        val registration = FilterRegistrationBean<Filter>()
        registration.setDispatcherTypes(DispatcherType.REQUEST)
        registration.setFilter(XssFilter())
        registration.addUrlPatterns(*StringUtils.split(xssProperties.urlPatterns, StringUtilsExtend.SEPARATOR))
        registration.setName("xssFilter")
        registration.order = FilterRegistrationBean.HIGHEST_PRECEDENCE
        val initParameters: MutableMap<String, String> = mutableMapOf()
        initParameters["excludes"] = xssProperties.excludes!!
        registration.initParameters = initParameters
        return registration
    }

    @Bean
    fun someFilterRegistration(): FilterRegistrationBean<out Filter> {
        val registration = FilterRegistrationBean<Filter>()
        registration.filter = RepeatableFilter()
        registration.addUrlPatterns("/*")
        registration.setName("repeatableFilter")
        registration.order = FilterRegistrationBean.LOWEST_PRECEDENCE
        return registration
    }
}
