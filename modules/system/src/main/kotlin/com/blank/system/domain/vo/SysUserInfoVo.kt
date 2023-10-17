package com.blank.system.domain.vo

/**
 * 用户信息
 */
class SysUserInfoVo {
    /**
     * 用户信息
     */
    var user: SysUserVo? = null

    /**
     * 角色ID列表
     */
    var roleIds: MutableList<Long>? = null

    /**
     * 角色列表
     */
    var roles: MutableList<SysRoleVo>? = null
}
