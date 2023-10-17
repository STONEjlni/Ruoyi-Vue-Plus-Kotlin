package com.blank.common.core.domain.dto

import java.io.Serializable

/**
 * 角色
 */
class RoleDTO : Serializable {
    /**
     * 角色ID
     */
    var roleId: Long? = null

    /**
     * 角色名称
     */
    var roleName: String? = null

    /**
     * 角色权限
     */
    var roleKey: String? = null

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    var dataScope: String? = null
}
