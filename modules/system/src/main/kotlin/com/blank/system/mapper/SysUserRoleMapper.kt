package com.blank.system.mapper

import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysUserRole

/**
 * 用户与角色关联表 数据层
 */
interface SysUserRoleMapper : BaseMapperPlus<SysUserRole, SysUserRole> {
    fun selectUserIdsByRoleId(roleId: Long?): MutableList<Long>?
}
