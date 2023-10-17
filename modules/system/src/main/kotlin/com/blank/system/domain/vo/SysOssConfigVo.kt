package com.blank.system.domain.vo

import com.blank.system.domain.SysOssConfig
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable

/**
 * 对象存储配置视图对象 sys_oss_config
 */
@AutoMapper(target = SysOssConfig::class)
class SysOssConfigVo : Serializable {
    /**
     * 主建
     */
    var ossConfigId: Long? = null

    /**
     * 配置key
     */
    var configKey: String? = null

    /**
     * accessKey
     */
    var accessKey: String? = null

    /**
     * 秘钥
     */
    var secretKey: String? = null

    /**
     * 桶名称
     */
    var bucketName: String? = null

    /**
     * 前缀
     */
    var prefix: String? = null

    /**
     * 访问站点
     */
    var endpoint: String? = null

    /**
     * 自定义域名
     */
    var domain: String? = null

    /**
     * 是否https（Y=是,N=否）
     */
    var isHttps: String? = null

    /**
     * 域
     */
    var region: String? = null

    /**
     * 是否默认（0=是,1=否）
     */
    var status: String? = null

    /**
     * 扩展字段
     */
    var ext1: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 桶权限类型(0private 1public 2custom)
     */
    var accessPolicy: String? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
