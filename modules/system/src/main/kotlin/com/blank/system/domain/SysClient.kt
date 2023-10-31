package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import java.io.Serial

/**
 * 授权管理对象 sys_client
 */
@Table("sys_client")
@Open
@NoArg
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

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Column(isLogicDelete = true)
    var delFlag: String? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
