package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.convert.Convert
import com.blank.common.core.utils.StreamUtils
import com.blank.common.mybatis.helper.DataBaseHelper
import com.blank.system.domain.SysDept
import com.blank.system.domain.SysRoleDept
import com.blank.system.domain.table.SysDeptDef.SYS_DEPT
import com.blank.system.domain.table.SysRoleDeptDef.SYS_ROLE_DEPT
import com.blank.system.mapper.SysDeptMapper
import com.blank.system.mapper.SysRoleDeptMapper
import com.blank.system.service.ISysDataScopeService
import com.mybatisflex.core.query.QueryWrapper
import org.springframework.stereotype.Service

/**
 * 数据权限 实现
 *
 *
 * 注意: 此Service内不允许调用标注`数据权限`注解的方法
 * 例如: deptMapper.selectList 此 selectList 方法标注了`数据权限`注解 会出现循环解析的问题
 */
@Service("sdss")
class SysDataScopeServiceImpl(
    private val roleDeptMapper: SysRoleDeptMapper,
    private val deptMapper: SysDeptMapper
) : ISysDataScopeService {

    override fun getRoleCustom(roleId: Long): String? {
        val list: MutableList<SysRoleDept> = roleDeptMapper.selectListByQuery(
            QueryWrapper.create().from(SYS_ROLE_DEPT).select(SYS_ROLE_DEPT.DEPT_ID)
                .where(SYS_ROLE_DEPT.ROLE_ID.eq(roleId))
        )
        return if (CollUtil.isNotEmpty(list)) {
            StreamUtils.join(list) { rd -> Convert.toStr(rd.deptId) }
        } else null
    }

    override fun getDeptAndChild(deptId: Long): String? {
        val deptList: MutableList<SysDept> = deptMapper.selectListByQuery(
            QueryWrapper.create().from(SYS_DEPT).select(SYS_DEPT.DEPT_ID)
                .where(DataBaseHelper.findInSet(deptId, "ancestors"))
        )
        val ids: MutableList<Long?> = StreamUtils.toList(deptList, SysDept::deptId)
        ids.add(deptId)
        return if (CollUtil.isNotEmpty(ids)) {
            StreamUtils.join(ids, Convert::toStr)
        } else null
    }
}
