package com.blank.common.encrypt.config

import com.blank.common.encrypt.filter.CryptoFilter
import com.blank.common.encrypt.properties.ApiDecryptProperties
import jakarta.servlet.DispatcherType
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean

/**
 * api 解密自动配置
 */
@AutoConfiguration
@EnableConfigurationProperties(ApiDecryptProperties::class)
@ConditionalOnProperty(value = ["api-decrypt.enabled"], havingValue = "true")
class ApiDecryptAutoConfiguration {
    @Bean
    fun cryptoFilterRegistration(properties: ApiDecryptProperties): FilterRegistrationBean<CryptoFilter> {
        val registration = FilterRegistrationBean<CryptoFilter>()
        registration.setDispatcherTypes(DispatcherType.REQUEST)
        registration.filter = CryptoFilter(properties)
        registration.addUrlPatterns("/*")
        registration.setName("cryptoFilter")
        registration.order = FilterRegistrationBean.HIGHEST_PRECEDENCE
        return registration
    }
}
