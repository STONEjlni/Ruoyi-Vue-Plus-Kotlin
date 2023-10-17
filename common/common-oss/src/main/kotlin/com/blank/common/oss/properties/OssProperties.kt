package com.blank.common.oss.properties

/**
 * OSS对象存储 配置属性
 *
 */
class OssProperties {
    /**
     * 访问站点
     */
    val endpoint: String? = null

    /**
     * 自定义域名
     */
    val domain: String? = null

    /**
     * 前缀
     */
    val prefix: String? = null

    /**
     * ACCESS_KEY
     */
    val accessKey: String? = null

    /**
     * SECRET_KEY
     */
    val secretKey: String? = null

    /**
     * 存储空间名
     */
    val bucketName: String? = null

    /**
     * 存储区域
     */
    val region: String? = null

    /**
     * 是否https（Y=是,N=否）
     */
    val isHttps: String? = null

    /**
     * 桶权限类型(0private 1public 2custom)
     */
    val accessPolicy: String? = null

    val tenantId: String
        get() = "blank"
}
