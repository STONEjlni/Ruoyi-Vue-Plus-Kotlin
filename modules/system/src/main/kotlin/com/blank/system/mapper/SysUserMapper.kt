package com.blank.system.mapper

import com.blank.common.mybatis.annotation.DataColumn
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysUser
import com.blank.system.domain.vo.SysUserVo
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper
import org.apache.ibatis.annotations.Param

/**
 * 用户表 数据层
 */
interface SysUserMapper : BaseMapperPlus<SysUser, SysUserVo> {
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["u.user_id"]
        )]
    )
    fun selectPageUserList(
        @Param("page") page: Page<SysUser?>?,  /*<SysUser>*/
        queryWrapper: QueryWrapper?
    ): Page<SysUserVo?>?

    /**
     * 根据条件分页查询用户列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["u.user_id"]
        )]
    )
    fun selectUserList( /*<SysUser>*/queryWrapper: QueryWrapper?): MutableList<SysUserVo>?

    /**
     * 根据条件分页查询已配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["u.user_id"]
        )]
    )
    fun selectAllocatedList(
        @Param("page") page: Page<SysUser>?,  /*<SysUser>*/
        queryWrapper: QueryWrapper?
    ): Page<SysUserVo>?

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param queryWrapper 查询条件
     * @return 用户信息集合信息
     */
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["u.user_id"]
        )]
    )
    fun selectUnallocatedList(
        @Param("page") page: Page<SysUser>?,  /*<SysUser>*/
        queryWrapper: QueryWrapper?
    ): Page<SysUserVo>?

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    fun selectUserByUserName(userName: String?): SysUserVo?

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    fun selectUserByPhonenumber(phonenumber: String?): SysUserVo?

    /**
     * 通过邮箱查询用户
     *
     * @param email 邮箱
     * @return 用户对象信息
     */
    fun selectUserByEmail(email: String?): SysUserVo?

    /**
     * 通过用户名查询用户(不走租户插件)
     *
     * @param userName 用户名
     * @param tenantId 租户id
     * @return 用户对象信息
     */
    fun selectTenantUserByUserName(
        @Param("userName") userName: String?,
        @Param("tenantId") tenantId: String?
    ): SysUserVo?

    /**
     * 通过手机号查询用户(不走租户插件)
     *
     * @param phonenumber 手机号
     * @param tenantId    租户id
     * @return 用户对象信息
     */
    fun selectTenantUserByPhonenumber(
        @Param("phonenumber") phonenumber: String?,
        @Param("tenantId") tenantId: String?
    ): SysUserVo?

    /**
     * 通过邮箱查询用户(不走租户插件)
     *
     * @param email    邮箱
     * @param tenantId 租户id
     * @return 用户对象信息
     */
    fun selectTenantUserByEmail(@Param("email") email: String?, @Param("tenantId") tenantId: String?): SysUserVo?

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    @DataPermission(
        [DataColumn(key = ["deptName"], value = ["d.dept_id"]), DataColumn(
            key = ["userName"],
            value = ["u.user_id"]
        )]
    )
    fun selectUserById(userId: Long?): SysUserVo? /*@Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    int update(SysUser user, QueryWrapper*/
    /*<SysUser>*/ /* updateWrapper);*/ /*@Override
    @DataPermission({
        @DataColumn(key = "deptName", value = "dept_id"),
        @DataColumn(key = "userName", value = "user_id")
    })
    int updateById(@Param(Constants.ENTITY) SysUser user);*/
}
