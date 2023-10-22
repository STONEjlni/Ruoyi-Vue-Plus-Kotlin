package com.blank.system.mapper

import com.blank.common.mybatis.annotation.DataColumn
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.system.domain.SysRole
import com.blank.system.domain.table.SysDeptDef.SYS_DEPT
import com.blank.system.domain.table.SysRoleDef.SYS_ROLE
import com.blank.system.domain.table.SysUserDef.SYS_USER
import com.blank.system.domain.table.SysUserRoleDef.SYS_USER_ROLE
import com.blank.system.domain.vo.SysRoleVo
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryMethods
import com.mybatisflex.core.query.QueryWrapper
import org.apache.ibatis.annotations.Param

/**
 * 角色表 数据层
 */
interface SysRoleMapper : BaseMapperPlus<SysRole> {
    fun selectPageRoleList(@Param("pageQuery") pageQuery: PageQuery, queryWrapper: QueryWrapper): Page<SysRoleVo> {
        selectRoleVo(queryWrapper)
        return paginateAs(
            pageQuery, queryWrapper, SysRoleVo::class.java, DataPermission.of(
                DataColumn.of("deptName", "d.dept_id"),
                DataColumn.of("userName", "r.create_by")
            )
        )
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @return 角色数据集合信息
     */
    fun selectRoleList(queryWrapper: QueryWrapper): MutableList<SysRoleVo> {
        selectRoleVo(queryWrapper)
        return this.selectListByQueryAs(
            queryWrapper, SysRoleVo::class.java, DataPermission.of(
                DataColumn.of("deptName", "d.dept_id"),
                DataColumn.of("userName", "r.create_by")
            )
        )
    }

    private fun selectRoleVo(queryWrapper: QueryWrapper) {
        queryWrapper.select(
            QueryMethods.distinct(SYS_ROLE.ROLE_ID),
            SYS_ROLE.ROLE_NAME,
            SYS_ROLE.ROLE_KEY,
            SYS_ROLE.ROLE_SORT,
            SYS_ROLE.DATA_SCOPE,
            SYS_ROLE.MENU_CHECK_STRICTLY,
            SYS_ROLE.DEPT_CHECK_STRICTLY,
            SYS_ROLE.STATUS,
            SYS_ROLE.DEL_FLAG,
            SYS_ROLE.CREATE_TIME,
            SYS_ROLE.REMARK
        ).from(SYS_ROLE.`as`("r"))
            .leftJoin<QueryWrapper>(SYS_USER_ROLE).`as`("sur").on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ROLE_ID))
            .leftJoin<QueryWrapper>(SYS_USER).`as`("u").on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
            .leftJoin<QueryWrapper>(SYS_DEPT).`as`("d").on(SYS_USER.DEPT_ID.eq(SYS_DEPT.DEPT_ID))
    }


    fun selectRoleById(@Param("roleId") roleId: Long): SysRoleVo {
        val queryWrapper: QueryWrapper = QueryWrapper.create().where(SYS_ROLE.ROLE_ID.eq(roleId))
        selectRoleVo(queryWrapper)
        return selectOneByQueryAs(
            queryWrapper, SysRoleVo::class.java, DataPermission.of(
                DataColumn.of("deptName", "d.dept_id"),
                DataColumn.of("userName", "r.create_by")
            )
        )
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    fun selectRolePermissionByUserId(userId: Long): MutableList<SysRoleVo> {
        val queryWrapper: QueryWrapper = QueryWrapper.create().where(SYS_USER_ROLE.USER_ID.eq(userId))
        selectRoleVo(queryWrapper)
        return selectListByQueryAs(queryWrapper, SysRoleVo::class.java)
    }


    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    fun selectRoleListByUserId(userId: Long): MutableList<Long> {
        val queryWrapper: QueryWrapper = QueryWrapper.create().select(SYS_ROLE.ROLE_ID).from(SYS_ROLE.`as`("r"))
            .leftJoin<QueryWrapper>(SYS_USER_ROLE).`as`("sur").on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ROLE_ID))
            .leftJoin<QueryWrapper>(SYS_USER).`as`("u").on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
            .where(SYS_USER.USER_ID.eq(userId))
        return selectListByQueryAs(queryWrapper, Long::class.java)
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userName 用户名
     * @return 角色列表
     */
    fun selectRolesByUserName(userName: String): MutableList<SysRoleVo> {
        val queryWrapper: QueryWrapper = QueryWrapper.create()
            .select(SYS_ROLE.ROLE_ID, SYS_ROLE.ROLE_NAME, SYS_ROLE.ROLE_KEY, SYS_ROLE.ROLE_SORT)
            .from(SYS_ROLE.`as`("r"))
            .leftJoin<QueryWrapper>(SYS_USER_ROLE).`as`("sur").on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ROLE_ID))
            .leftJoin<QueryWrapper>(SYS_USER).`as`("u").on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
            .where(SYS_USER.USER_NAME.eq(userName))
        return selectListByQueryAs(queryWrapper, SysRoleVo::class.java)
    }
}
