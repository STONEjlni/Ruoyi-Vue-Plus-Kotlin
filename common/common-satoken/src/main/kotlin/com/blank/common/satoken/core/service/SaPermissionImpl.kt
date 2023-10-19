package com.blank.common.satoken.core.service

import cn.dev33.satoken.stp.StpInterface
import com.blank.common.core.annotation.UserType
import com.blank.common.core.annotation.UserType.Companion.getUserType
import com.blank.common.satoken.utils.LoginHelper

/**
 * sa-token 权限管理实现类
 */
class SaPermissionImpl : StpInterface {
    /**
     * 获取菜单权限列表
     */
    override fun getPermissionList(loginId: Any, loginType: String?): MutableList<String> {
        val loginUser = LoginHelper.getLoginUser()
        val userType = getUserType(loginUser?.userType!!)
        if (userType === UserType.SYS_USER) {
            return loginUser.menuPermission.toMutableList()
        } else if (userType === UserType.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return ArrayList()
    }

    /**
     * 获取角色权限列表
     */
    override fun getRoleList(loginId: Any, loginType: String?): MutableList<String> {
        val loginUser = LoginHelper.getLoginUser()
        val userType = getUserType(loginUser?.userType!!)
        if (userType === UserType.SYS_USER) {
            return loginUser.rolePermission.toMutableList()
        } else if (userType === UserType.APP_USER) {
            // 其他端 自行根据业务编写
        }
        return ArrayList()
    }
}
