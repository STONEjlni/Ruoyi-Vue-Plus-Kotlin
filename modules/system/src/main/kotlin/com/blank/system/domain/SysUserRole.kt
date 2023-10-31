package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 用户和角色关联 sys_user_role
 */
@Table("sys_user_role")
@Open
@NoArg
class SysUserRole {
    /**
     * 用户ID
     */
    @Id
    var userId: Long? = null

    /**
     * 角色ID
     */
    var roleId: Long? = null
}
