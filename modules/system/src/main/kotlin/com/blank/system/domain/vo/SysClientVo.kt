package com.blank.system.domain.vo

import com.blank.system.domain.SysClient
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable

/**
 * 授权管理视图对象 sys_client
 */
@AutoMapper(target = SysClient::class)
class SysClientVo : Serializable {
    /**
     * id
     */
    var id: Long? = null

    /**
     * 客户端id
     */
    var clientId: String? = null

    /**
     * 客户端key
     */
    var clientKey: String? = null

    /**
     * 客户端秘钥
     */
    var clientSecret: String? = null

    /**
     * 授权类型
     */
    var grantTypeList: MutableList<String>? = null

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

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
