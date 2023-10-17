package com.blank.common.encrypt.config

import com.blank.common.encrypt.core.EncryptorManager
import com.blank.common.encrypt.interceptor.MybatisDecryptInterceptor
import com.blank.common.encrypt.interceptor.MybatisEncryptInterceptor
import com.blank.common.encrypt.properties.EncryptorProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean

/**
 * 加解密配置
 *
 */
@AutoConfiguration
@EnableConfigurationProperties(EncryptorProperties::class)
@ConditionalOnProperty(value = ["mybatis-encryptor.enable"], havingValue = "true")
class EncryptorAutoConfiguration(
    val properties: EncryptorProperties
) {

    @Bean
    fun encryptorManager(): EncryptorManager {
        return EncryptorManager()
    }

    @Bean
    fun mybatisEncryptInterceptor(encryptorManager: EncryptorManager): MybatisEncryptInterceptor {
        return MybatisEncryptInterceptor(encryptorManager, properties)
    }

    @Bean
    fun mybatisDecryptInterceptor(encryptorManager: EncryptorManager): MybatisDecryptInterceptor {
        return MybatisDecryptInterceptor(encryptorManager, properties)
    }
}
