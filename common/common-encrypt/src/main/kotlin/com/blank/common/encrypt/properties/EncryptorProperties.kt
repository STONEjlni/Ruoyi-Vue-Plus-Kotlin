package com.blank.common.encrypt.properties

import com.blank.common.encrypt.enumd.AlgorithmType
import com.blank.common.encrypt.enumd.EncodeType
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 加解密属性配置类
 *
 */
@ConfigurationProperties(prefix = "mybatis-encryptor")
class EncryptorProperties {
    /**
     * 过滤开关
     */
    val enable: Boolean? = null

    /**
     * 默认算法
     */
    val algorithm: AlgorithmType? = null

    /**
     * 安全秘钥
     */
    val password: String? = null

    /**
     * 公钥
     */
    val publicKey: String? = null

    /**
     * 私钥
     */
    val privateKey: String? = null

    /**
     * 编码方式，base64/hex
     */
    val encode: EncodeType? = null
}
