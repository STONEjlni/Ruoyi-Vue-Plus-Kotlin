package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.domain.vo.SysUserVo

/**
 * 用户 业务层
 */
interface ISysUserService {
    fun selectPageUserList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>?

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    fun selectUserList(user: SysUserBo): MutableList<SysUserVo>?

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    fun selectAllocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>?

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    fun selectUnallocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>?

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    fun selectUserByUserName(userName: String): SysUserVo?

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    fun selectUserByPhonenumber(phonenumber: String): SysUserVo?

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    fun selectUserById(userId: Long): SysUserVo?

    /**
     * 根据用户ID查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    fun selectUserRoleGroup(userName: String): String?

    /**
     * 根据用户ID查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    fun selectUserPostGroup(userName: String): String?

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    fun checkUserNameUnique(user: SysUserBo): Boolean

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    fun checkPhoneUnique(user: SysUserBo): Boolean

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    fun checkEmailUnique(user: SysUserBo): Boolean

    /**
     * 校验用户是否允许操作
     *
     * @param userId 用户ID
     */
    fun checkUserAllowed(userId: Long)

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    fun checkUserDataScope(userId: Long)

    /**
     * 新增用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    fun insertUser(user: SysUserBo): Int

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    fun registerUser(user: SysUserBo): Boolean

    /**
     * 修改用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    fun updateUser(user: SysUserBo): Int

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    fun insertUserAuth(userId: Long, roleIds: Array<Long>)

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 帐号状态
     * @return 结果
     */
    fun updateUserStatus(userId: Long, status: String): Int

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    fun updateUserProfile(user: SysUserBo): Int

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    fun updateUserAvatar(userId: Long, avatar: Long): Boolean

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    fun resetUserPwd(userId: Long, password: String): Int

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    fun deleteUserById(userId: Long): Int

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    fun deleteUserByIds(userIds: Array<Long>): Int

    /**
     * 通过部门id查询当前部门所有用户
     *
     * @param deptId
     * @return
     */
    fun selectUserListByDept(deptId: Long): MutableList<SysUserVo>?
}
