package com.blank.system.domain.bo

import com.blank.common.core.validate.AddGroup
import com.blank.common.core.validate.EditGroup
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysOssConfig
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 对象存储配置业务对象 sys_oss_config
 */
@AutoMapper(target = SysOssConfig::class, reverseConvertGenerate = false)
class SysOssConfigBo : BaseEntity() {
    /**
     * 主建
     */
    var ossConfigId: @NotNull(message = "主建不能为空", groups = [EditGroup::class]) Long? = null

    /**
     * 配置key
     */
    var configKey: @NotBlank(message = "配置key不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 2,
        max = 100,
        message = "configKey长度必须介于{min}和{max} 之间"
    ) String? = null

    /**
     * accessKey
     */
    var accessKey: @NotBlank(message = "accessKey不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 2,
        max = 100,
        message = "accessKey长度必须介于{min}和{max} 之间"
    ) String? = null

    /**
     * 秘钥
     */
    var secretKey: @NotBlank(message = "secretKey不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 2,
        max = 100,
        message = "secretKey长度必须介于{min}和{max} 之间"
    ) String? = null

    /**
     * 桶名称
     */
    var bucketName: @NotBlank(message = "桶名称不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 2,
        max = 100,
        message = "bucketName长度必须介于{min}和{max}之间"
    ) String? = null

    /**
     * 前缀
     */
    var prefix: String? = null

    /**
     * 访问站点
     */
    var endpoint: @NotBlank(message = "访问站点不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 2,
        max = 100,
        message = "endpoint长度必须介于{min}和{max}之间"
    ) String? = null

    /**
     * 自定义域名
     */
    var domain: String? = null

    /**
     * 是否https（Y=是,N=否）
     */
    var isHttps: String? = null

    /**
     * 是否默认（0=是,1=否）
     */
    var status: String? = null

    /**
     * 域
     */
    var region: String? = null

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
    var accessPolicy: @NotBlank(
        message = "桶权限类型不能为空",
        groups = [AddGroup::class, EditGroup::class]
    ) String? = null

    fun setIsHttps(isHttps: String?) {
        this.isHttps = isHttps
    }

    fun getIsHttps() = this.isHttps
}
