package com.blank.common.core.config

import jakarta.validation.Validator
import org.hibernate.validator.HibernateValidator
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.util.*

/**
 * 校验框架配置类
 */
@AutoConfiguration
class ValidatorConfig {
    /**
     * 配置校验框架 快速返回模式
     */
    @Bean
    fun validator(messageSource: MessageSource?): Validator {
        LocalValidatorFactoryBean().use { factoryBean ->
            // 国际化
            factoryBean.setValidationMessageSource(messageSource!!)
            // 设置使用 HibernateValidator 校验器
            factoryBean.setProviderClass(HibernateValidator::class.java)
            val properties = Properties()
            // 设置 快速异常返回
            properties.setProperty("hibernate.validatoR.Companion.fail_fast", "true")
            factoryBean.setValidationProperties(properties)
            // 加载配置
            factoryBean.afterPropertiesSet()
            return factoryBean.validator
        }
    }
}
