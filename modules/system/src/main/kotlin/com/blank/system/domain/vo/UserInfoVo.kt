package com.blank.system.domain.vo

/**
 * 登录用户信息
 */
class UserInfoVo {
    /**
     * 用户基本信息
     */
    var user: SysUserVo? = null

    /**
     * 菜单权限
     */
    var permissions: MutableSet<String>? = null

    /**
     * 角色权限
     */
    var roles: MutableSet<String>? = null
}
