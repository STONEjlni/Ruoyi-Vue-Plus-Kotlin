package com.blank.common.mail.config

import com.blank.common.mail.config.properties.MailProperties
import com.blank.common.mail.utils.MailAccount
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * JavaMail 配置
 */
@AutoConfiguration
@EnableConfigurationProperties(MailProperties::class)
class MailConfig {
    @Bean
    @ConditionalOnProperty(value = ["mail.enabled"], havingValue = "true")
    fun mailAccount(mailProperties: MailProperties): MailAccount {
        val account = MailAccount()
        account.setHost(mailProperties.host)
        account.setPort(mailProperties.port)
        account.setAuth(mailProperties.auth)
        account.setFrom(mailProperties.from)
        account.setUser(mailProperties.user)
        account.setPass(mailProperties.pass)
        account.setSocketFactoryPort(mailProperties.port!!)
        account.setStarttlsEnable(mailProperties.starttlsEnable)
        account.setSslEnable(mailProperties.sslEnable)
        account.setTimeout(mailProperties.timeout!!)
        account.setConnectionTimeout(mailProperties.connectionTimeout!!)
        return account
    }
}
