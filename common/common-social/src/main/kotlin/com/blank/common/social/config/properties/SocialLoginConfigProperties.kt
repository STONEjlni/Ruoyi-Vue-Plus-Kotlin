package com.blank.common.social.config.properties

/**
 * 社交登录配置
 */
class SocialLoginConfigProperties {
    /**
     * 应用 ID
     */
    val clientId: String? = null

    /**
     * 应用密钥
     */
    val clientSecret: String? = null

    /**
     * 回调地址
     */
    val redirectUri: String? = null

    /**
     * 是否获取unionId
     */
    val unionId = false

    /**
     * Coding 企业名称
     */
    val codingGroupName: String? = null

    /**
     * 支付宝公钥
     */
    val alipayPublicKey: String? = null

    /**
     * 企业微信应用ID
     */
    val agentId: String? = null

    /**
     * stackoverflow api key
     */
    val stackOverflowKey: String? = null

    /**
     * 设备ID
     */
    val deviceId: String? = null

    /**
     * 客户端系统类型
     */
    val clientOsType: String? = null

    /**
     * maxkey 服务器地址
     */
    val serverUrl: String? = null
}
