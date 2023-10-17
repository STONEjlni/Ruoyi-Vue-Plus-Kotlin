package com.blank.system.service.impl

import com.blank.common.core.constant.UserConstants
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.system.service.ISysMenuService
import com.blank.system.service.ISysPermissionService
import com.blank.system.service.ISysRoleService
import org.springframework.stereotype.Service

/**
 * 用户权限处理
 */
@Service
class SysPermissionServiceImpl(
    private val roleService: ISysRoleService,
    private val menuService: ISysMenuService
) : ISysPermissionService {


    /**
     * 获取角色数据权限
     *
     * @param userId 用户id
     * @return 角色权限信息
     */
    override fun getRolePermission(userId: Long): MutableSet<String>? {
        val roles: MutableSet<String> = HashSet()
        // 管理员拥有所有权限
        if (isSuperAdmin(userId)) {
            roles.add(UserConstants.SUPER_ADMIN_ROLE_KEY)
        } else {
            roles.addAll(roleService.selectRolePermissionByUserId(userId)!!)
        }
        return roles
    }

    /**
     * 获取菜单数据权限
     *
     * @param userId 用户id
     * @return 菜单权限信息
     */
    override fun getMenuPermission(userId: Long): MutableSet<String>? {
        val perms: MutableSet<String> = HashSet()
        // 管理员拥有所有权限
        if (isSuperAdmin(userId)) {
            perms.add("*:*:*")
        } else {
            perms.addAll(menuService.selectMenuPermsByUserId(userId)!!)
        }
        return perms
    }
}
