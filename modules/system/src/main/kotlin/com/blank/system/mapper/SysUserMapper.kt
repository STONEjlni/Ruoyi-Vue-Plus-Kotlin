package com.blank.system.mapper

import com.blank.common.core.utils.MapstructUtils
import com.blank.common.mybatis.annotation.DataColumn
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.system.domain.SysUser
import com.blank.system.domain.dto.SysUserDto
import com.blank.system.domain.table.SysDeptDef.SYS_DEPT
import com.blank.system.domain.table.SysRoleDef.SYS_ROLE
import com.blank.system.domain.table.SysUserDef.SYS_USER
import com.blank.system.domain.table.SysUserRoleDef.SYS_USER_ROLE
import com.blank.system.domain.vo.SysUserVo
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.update.UpdateChain

/**
 * 用户表 数据层
 */
interface SysUserMapper : BaseMapperPlus<SysUser> {
    fun selectPageUserList(pageQuery: PageQuery, queryWrapper: QueryWrapper): Page<SysUserVo> {
        val sysUserDtoPage: Page<SysUserDto> = this.paginateAs(
            pageQuery, queryWrapper,
            SysUserDto::class.java, DataPermission.of(
                DataColumn.of("deptName", "d.dept_id"),
                DataColumn.of("userName", "u.user_id")
            )
        )
        val p: Page<SysUserVo> = Page.of(pageQuery.pageNum, pageQuery.pageSize, sysUserDtoPage.totalRow)
        val records: MutableList<SysUserVo>? = MapstructUtils.convert(sysUserDtoPage.records, SysUserVo::class.java)
        p.setRecords(records)
        return p
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    fun selectUserList(queryWrapper: QueryWrapper): MutableList<SysUserVo> {
        val sysUserDtos: MutableList<SysUserDto> = this.selectListByQueryAs(
            queryWrapper,
            SysUserDto::class.java, DataPermission.of(
                DataColumn.of("deptName", "d.dept_id"),
                DataColumn.of("userName", "u.user_id")
            )
        )
        return MapstructUtils.convert(sysUserDtos, SysUserVo::class.java)!!
    }

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    fun selectAllocatedList(page: PageQuery, queryWrapper: QueryWrapper): Page<SysUserVo> {
        val sysUserDtoPage: Page<SysUserDto> = this.paginateAs(
            page,
            queryWrapper,
            SysUserDto::class.java,
            DataPermission.of(DataColumn.of("deptName", "d.dept_id"), DataColumn.of("userName", "u.user_id"))
        )
        val p: Page<SysUserVo> = Page.of(page.pageNum, page.pageSize, sysUserDtoPage.totalRow)
        val records: MutableList<SysUserVo>? = MapstructUtils.convert(sysUserDtoPage.records, SysUserVo::class.java)
        p.setRecords(records)
        return p
    }

    private fun selectUserVo(queryWrapper: QueryWrapper) {
        queryWrapper.select(
            SYS_USER.USER_ID,
            SYS_USER.DEPT_ID,
            SYS_USER.USER_NAME,
            SYS_USER.NICK_NAME,
            SYS_USER.USER_TYPE,
            SYS_USER.EMAIL,
            SYS_USER.AVATAR,
            SYS_USER.PHONENUMBER,
            SYS_USER.PASSWORD,
            SYS_USER.SEX,
            SYS_USER.STATUS,
            SYS_USER.DEL_FLAG,
            SYS_USER.LOGIN_IP,
            SYS_USER.LOGIN_DATE,
            SYS_USER.CREATE_BY,
            SYS_USER.CREATE_TIME,
            SYS_USER.REMARK,
            SYS_DEPT.DEPT_ID,
            SYS_DEPT.PARENT_ID,
            SYS_DEPT.ANCESTORS,
            SYS_DEPT.DEPT_NAME,
            SYS_DEPT.ORDER_NUM,
            SYS_DEPT.LEADER,
            SYS_DEPT.STATUS,
            SYS_ROLE.ROLE_ID,
            SYS_ROLE.ROLE_NAME,
            SYS_ROLE.ROLE_KEY,
            SYS_ROLE.ROLE_SORT,
            SYS_ROLE.DATA_SCOPE,
            SYS_ROLE.STATUS
        ).from(SYS_USER).`as`("u")
            .leftJoin<QueryWrapper>(SYS_DEPT).`as`("d").on(SYS_USER.DEPT_ID.eq(SYS_DEPT.DEPT_ID))
            .leftJoin<QueryWrapper>(SYS_USER_ROLE).`as`("sur").on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
            .leftJoin<QueryWrapper>(SYS_ROLE).`as`("r").on(SYS_ROLE.ROLE_ID.eq(SYS_USER_ROLE.ROLE_ID))
    }


    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    fun selectUserByUserName(userName: String): SysUserVo {
        val queryWrapper: QueryWrapper = QueryWrapper.create().where(SYS_USER.USER_NAME.eq(userName))
        selectUserVo(queryWrapper)
        val sysUserDto: SysUserDto = selectOneByQueryAs(queryWrapper, SysUserDto::class.java)
        return MapstructUtils.convert(sysUserDto, SysUserVo::class.java)!!
    }

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    fun selectUserByPhonenumber(phonenumber: String): SysUserVo {
        val queryWrapper: QueryWrapper = QueryWrapper.create().where(SYS_USER.PHONENUMBER.eq(phonenumber))
        selectUserVo(queryWrapper)
        val sysUserDto: SysUserDto = selectOneByQueryAs(queryWrapper, SysUserDto::class.java)
        return MapstructUtils.convert(sysUserDto, SysUserVo::class.java)!!
    }

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    fun selectUserByEmail(email: String): SysUserVo {
        val queryWrapper: QueryWrapper = QueryWrapper.create().where(SYS_USER.EMAIL.eq(email))
        selectUserVo(queryWrapper)
        val sysUserDto: SysUserDto = selectOneByQueryAs(queryWrapper, SysUserDto::class.java)
        return MapstructUtils.convert(sysUserDto, SysUserVo::class.java)!!
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    fun selectUserById(userId: Long): SysUserVo {
        val queryWrapper: QueryWrapper =
            QueryWrapper.create()
                .where(SYS_USER.USER_ID.eq(userId))
        selectUserVo(queryWrapper)
        val sysUserDto: SysUserDto = selectOneByQueryAs(
            queryWrapper,
            SysUserDto::class.java,
            DataPermission.of(DataColumn.of("deptName", "d.dept_id"), DataColumn.of("userName", "u.user_id"))
        )
        return MapstructUtils.convert(sysUserDto, SysUserVo::class.java)!!
    }


    fun update(updateChain: UpdateChain<SysUser>): Boolean {
        return this.update(
            updateChain,
            DataPermission.of(DataColumn.of("deptName", "dept_id"), DataColumn.of("userName", "user_id"))
        )
    }
}
