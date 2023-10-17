package com.blank.system.domain.bo

import com.blank.common.core.validate.AddGroup
import com.blank.common.core.validate.EditGroup
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysClient
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * 授权管理业务对象 sys_client
 */
@AutoMapper(target = SysClient::class, reverseConvertGenerate = false)
class SysClientBo : BaseEntity() {
    /**
     * id
     */
    var id: @NotNull(message = "id不能为空", groups = [EditGroup::class]) Long? = null

    /**
     * 客户端id
     */
    var clientId: String? = null

    /**
     * 客户端key
     */
    var clientKey: @NotBlank(
        message = "客户端key不能为空",
        groups = [AddGroup::class, EditGroup::class]
    ) String? = null

    /**
     * 客户端秘钥
     */
    var clientSecret: @NotBlank(
        message = "客户端秘钥不能为空",
        groups = [AddGroup::class, EditGroup::class]
    ) String? = null

    /**
     * 授权类型
     */
    var grantTypeList: @NotNull(
        message = "授权类型不能为空",
        groups = [AddGroup::class, EditGroup::class]
    ) MutableList<String>? = null

    /**
     * 授权类型
     */
    var grantType: String? = null

    /**
     * 设备类型
     */
    var deviceType: String? = null

    /**
     * token活跃超时时间
     */
    var activeTimeout: Long? = null

    /**
     * token固定超时时间
     */
    var timeout: Long? = null

    /**
     * 状态（0正常 1停用）
     */
    var status: String? = null
}
