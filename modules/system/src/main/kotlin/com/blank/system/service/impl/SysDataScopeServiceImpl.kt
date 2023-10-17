package com.blank.system.service.impl

import com.blank.system.mapper.SysDeptMapper
import com.blank.system.mapper.SysRoleDeptMapper
import com.blank.system.service.ISysDataScopeService
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
        /*List<SysRoleDept> list = roleDeptMapper.selectList(
            new LambdaQueryWrapper<SysRoleDept>()
                .select(SysRoleDept::getDeptId)
                .eq(SysRoleDept::getRoleId, roleId));
        if (CollUtil.isNotEmpty(list)) {
            return StreamUtils.join(list, rd -> Convert.toStr(rd.getDeptId()));
        }
        return null;*/
        return null
    }

    override fun getDeptAndChild(deptId: Long): String? {
        /*List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptId)
            .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
        ids.add(deptId);
        if (CollUtil.isNotEmpty(ids)) {
            return StreamUtils.join(ids, Convert::toStr);
        }
        return null;*/
        return null
    }
}
