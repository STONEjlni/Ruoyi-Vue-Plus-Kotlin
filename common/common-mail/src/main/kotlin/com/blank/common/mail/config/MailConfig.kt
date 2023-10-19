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
        account.host = mailProperties.host
        account.port = mailProperties.port
        account.auth = mailProperties.auth
        account.from = mailProperties.from
        account.user = mailProperties.user
        account.pass = mailProperties.pass
        account.socketFactoryPort = mailProperties.port!!
        account.starttlsEnable = mailProperties.starttlsEnable
        account.sslEnable = mailProperties.sslEnable
        account.timeout = mailProperties.timeout!!
        account.connectionTimeout = mailProperties.connectionTimeout!!
        return account
    }
}
