package com.blank.system.domain

import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import java.io.Serial

/**
 * 授权管理对象 sys_client
 */
@Table("sys_client")
class SysClient : BaseEntity() {
    /**
     * id
     */
    @Id
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
