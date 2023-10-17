package com.blank.system.domain

import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 角色表 sys_role
 */
@Table("sys_role")
class SysRole(
    /**
     * 角色ID
     */
    @field:Id var roleId: Long
) : BaseEntity() {
    /**
     * 角色名称
     */
    var roleName: String? = null

    /**
     * 角色权限
     */
    var roleKey: String? = null

    /**
     * 角色排序
     */
    var roleSort: Int? = null

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    var dataScope: String? = null

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    var menuCheckStrictly: Boolean? = null

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
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
}
