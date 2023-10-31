package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 角色和菜单关联 sys_role_menu
 */
@Table("sys_role_menu")
@Open
@NoArg
class SysRoleMenu {
    /**
     * 角色ID
     */
    @Id
    var roleId: Long? = null

    /**
     * 菜单ID
     */
    var menuId: Long? = null
}
