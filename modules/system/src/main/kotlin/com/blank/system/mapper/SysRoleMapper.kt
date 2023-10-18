package com.blank.system.mapper

import com.blank.common.mybatis.annotation.DataColumn
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysRole
import com.blank.system.domain.vo.SysRoleVo
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper

/**
 * 角色表 数据层
 */
interface SysRoleMapper : BaseMapperPlus<SysRole, SysRoleVo> {
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["r.create_by"]
        )]
    )
    fun selectPageRoleList(page: Page<SysRole>,  /*<SysRole>*/queryWrapper: QueryWrapper): Page<SysRoleVo>?

    /**
     * 根据条件分页查询角色数据
     *
     * @param queryWrapper 查询条件
     * @return 角色数据集合信息
     */
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["r.create_by"]
        )]
    )
    fun selectRoleList( /*<SysRole>*/queryWrapper: QueryWrapper): MutableList<SysRoleVo>?

    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["r.create_by"]
        )]
    )
    fun selectRoleById(roleId: Long): SysRoleVo?

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    fun selectRolePermissionByUserId(userId: Long): MutableList<SysRoleVo>?

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    fun selectRoleListByUserId(userId: Long): MutableList<Long>?

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    fun selectRolesByUserName(userName: String): MutableList<SysRoleVo>?
}
