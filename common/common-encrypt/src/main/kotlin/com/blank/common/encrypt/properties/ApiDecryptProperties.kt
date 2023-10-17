package com.blank.common.encrypt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * api解密属性配置类
 */
@ConfigurationProperties(prefix = "api-decrypt")
class ApiDecryptProperties {
    /**
     * 加密开关
     */
    val enabled: Boolean? = null

    /**
     * 头部标识
     */
    val headerFlag: String? = null


    /**
     * 公钥
     */
    val publicKey: String? = null

    /**
     * 私钥
     */
    val privateKey: String? = null
}
