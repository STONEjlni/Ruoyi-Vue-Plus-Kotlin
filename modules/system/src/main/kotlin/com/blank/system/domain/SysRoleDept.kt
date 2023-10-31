package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 角色和部门关联 sys_role_dept
 */
@Table("sys_role_dept")
@Open
@NoArg
class SysRoleDept {
    /**
     * 角色ID
     */
    @Id
    var roleId: Long? = null

    /**
     * 部门ID
     */
    var deptId: Long? = null
}
