package com.blank.system.domain.bo

import com.blank.common.core.constant.UserConstants
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysRole
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 角色信息业务对象 sys_role
 */
@AutoMapper(target = SysRole::class, reverseConvertGenerate = false)
class SysRoleBo(
    /**
     * 角色ID
     */
    var roleId: Long?
) : BaseEntity() {

    constructor() : this(null)

    /**
     * 角色名称
     */
    var roleName: @NotBlank(message = "角色名称不能为空") @Size(
        min = 0,
        max = 30,
        message = "角色名称长度不能超过{max}个字符"
    ) String? = null

    /**
     * 角色权限字符串
     */
    var roleKey: @NotBlank(
        message = "角色权限字符串不能为空"
    ) @Size(min = 0, max = 100, message = "权限字符长度不能超过{max}个字符") String? = null

    /**
     * 显示顺序
     */
    var roleSort: @NotNull(message = "显示顺序不能为空") Int? =
        null

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
     * 菜单组
     */
    var menuIds: Array<Long>? = arrayOf()

    /**
     * 部门组（数据权限）
     */
    var deptIds: Array<Long>? = arrayOf()
    val isSuperAdmin: Boolean
        get() = UserConstants.SUPER_ADMIN_ID == roleId
}
