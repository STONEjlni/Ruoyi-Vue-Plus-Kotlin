package com.blank.common.social.config.properties

/**
 * 社交登录配置
 */
class SocialLoginConfigProperties {
    /**
     * 应用 ID
     */
    var clientId: String? = null

    /**
     * 应用密钥
     */
    var clientSecret: String? = null

    /**
     * 回调地址
     */
    var redirectUri: String? = null

    /**
     * 是否获取unionId
     */
    var unionId = false

    /**
     * Coding 企业名称
     */
    var codingGroupName: String? = null

    /**
     * 支付宝公钥
     */
    var alipayPublicKey: String? = null

    /**
     * 企业微信应用ID
     */
    var agentId: String? = null

    /**
     * stackoverflow api key
     */
    var stackOverflowKey: String? = null

    /**
     * 设备ID
     */
    var deviceId: String? = null

    /**
     * 客户端系统类型
     */
    var clientOsType: String? = null

    /**
     * maxkey 服务器地址
     */
    var serverUrl: String? = null
}
