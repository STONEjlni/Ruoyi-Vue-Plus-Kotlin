package com.blank.system.domain.vo

import com.blank.common.core.constant.UserConstants
import com.blank.system.domain.SysRole
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 角色信息视图对象 sys_role
 */
@AutoMapper(target = SysRole::class)
class SysRoleVo : Serializable {
    /**
     * 角色ID
     */
    var roleId: Long? = null

    /**
     * 角色名称
     */
    var roleName: String? = null

    /**
     * 角色权限字符串
     */
    var roleKey: String? = null

    /**
     * 显示顺序
     */
    var roleSort: Int? = null

    /**
     * 数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）
     */
    var dataScope: String? = null

    /**
     * 菜单树选择项是否关联显示
     */
    var menuCheckStrictly: Boolean? = null

    /**
     * 部门树选择项是否关联显示
     */
    var deptCheckStrictly: Boolean? = null

    /**
     * 角色状态（0正常 1停用）
     */
    var status: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 创建时间
     */
    var createTime: Date? = null

    /**
     * 用户是否存在此角色标识 默认不存在
     */
    var flag = false
    val isSuperAdmin: Boolean
        get() = UserConstants.SUPER_ADMIN_ID == roleId

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
