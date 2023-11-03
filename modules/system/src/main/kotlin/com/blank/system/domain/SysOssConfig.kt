package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 对象存储配置对象 sys_oss_config
 *
 */
@Table("sys_oss_config")
@Open
@NoArg
class SysOssConfig : BaseEntity() {
    /**
     * 主建
     */
    @Id
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
     * 是否https（0否 1是）
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

    fun setIsHttps(isHttps: String?) {
        this.isHttps = isHttps
    }

    fun getIsHttps() = this.isHttps
}
