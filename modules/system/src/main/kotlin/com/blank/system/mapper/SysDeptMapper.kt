package com.blank.system.mapper

import com.blank.common.mybatis.annotation.DataColumn
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysDept
import com.blank.system.domain.vo.SysDeptVo
import com.mybatisflex.core.query.QueryWrapper
import org.apache.ibatis.annotations.Param

/**
 * 部门管理 数据层
 */
interface SysDeptMapper : BaseMapperPlus<SysDept, SysDeptVo> {
    /**
     * 查询部门管理数据
     *
     * @param queryWrapper 查询条件
     * @return 部门信息集合
     */
    @DataPermission([DataColumn(key = ["deptName"], value = ["dept_id"])])
    fun selectDeptList(queryWrapper: QueryWrapper?): MutableList<SysDeptVo>?

    @DataPermission([DataColumn(key = ["deptName"], value = ["dept_id"])])
    fun selectDeptById(deptId: Long?): SysDeptVo?

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId            角色ID
     * @param deptCheckStrictly 部门树选择项是否关联显示
     * @return 选中部门列表
     */
    fun selectDeptListByRoleId(
        @Param("roleId") roleId: Long?,
        @Param("deptCheckStrictly") deptCheckStrictly: Boolean
    ): MutableList<Long>?
}
