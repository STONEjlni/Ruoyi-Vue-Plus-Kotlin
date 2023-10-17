package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.dev33.satoken.secure.BCrypt
import cn.hutool.core.lang.tree.Tree
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.utils.StreamUtils.filter
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.satoken.utils.LoginHelper.getLoginUser
import com.blank.common.satoken.utils.LoginHelper.getUserId
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysDeptBo
import com.blank.system.domain.bo.SysRoleBo
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.domain.vo.SysRoleVo
import com.blank.system.domain.vo.SysUserInfoVo
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.domain.vo.UserInfoVo
import com.blank.system.service.ISysDeptService
import com.blank.system.service.ISysRoleService
import com.blank.system.service.ISysUserService
import jakarta.validation.constraints.NotNull
import org.apache.commons.lang3.StringUtils
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 用户信息
 */
@Validated
@RestController
@RequestMapping("/system/user")
class SysUserController(
    private val userService: ISysUserService,
    private val roleService: ISysRoleService,
    private val deptService: ISysDeptService
) : BaseController() {


    /**
     * 获取用户列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/list")
    fun list(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>? {
        return userService.selectPageUserList(user, pageQuery)
    }

    @get:GetMapping("/getInfo")
    val info: R<UserInfoVo>
        /**
         * 获取用户信息
         *
         * @return 用户信息
         */
        get() {
            val userInfoVo = UserInfoVo()
            val loginUser = getLoginUser()
            val user = userService.selectUserById(loginUser!!.userId!!)
            if (ObjectUtil.isNull(user)) {
                return fail("没有权限访问用户数据!")
            }
            userInfoVo.user = user
            userInfoVo.permissions = loginUser.menuPermission
            userInfoVo.roles = loginUser.rolePermission
            return ok(userInfoVo)
        }

    /**
     * 根据用户编号获取详细信息
     *
     * @param userId 用户ID
     */
    @SaCheckPermission("system:user:query")
    @GetMapping(value = ["/", "/{userId}"])
    fun getInfo(@PathVariable(value = "userId", required = false) userId: Long): R<SysUserInfoVo> {
        userService.checkUserDataScope(userId)
        val userInfoVo = SysUserInfoVo()
        val roleBo = SysRoleBo()
        roleBo.status = UserConstants.ROLE_NORMAL
        val roles = roleService.selectRoleList(roleBo)
        userInfoVo.roles = if (isSuperAdmin(userId)) roles else filter(roles!!) { r: SysRoleVo? -> !r!!.isSuperAdmin }
        if (ObjectUtil.isNotNull(userId)) {
            val sysUser = userService.selectUserById(userId)!!
            userInfoVo.user = sysUser
            userInfoVo.roleIds = sysUser.roles!!.map { obj: SysRoleVo -> obj.roleId!! }.toMutableList()
        }
        return ok(userInfoVo)
    }

    /**
     * 新增用户
     */
    @SaCheckPermission("system:user:add")
    @Log(title = "用户管理", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody user: SysUserBo): R<Unit> {
        deptService.checkDeptDataScope(user.deptId!!)
        if (!userService.checkUserNameUnique(user)) {
            return fail("新增用户'" + user.userName + "'失败，登录账号已存在")
        } else if (StringUtils.isNotEmpty(user.phonenumber) && !userService.checkPhoneUnique(user)) {
            return fail("新增用户'" + user.userName + "'失败，手机号码已存在")
        } else if (StringUtils.isNotEmpty(user.email) && !userService.checkEmailUnique(user)) {
            return fail("新增用户'" + user.userName + "'失败，邮箱账号已存在")
        }
        user.password = BCrypt.hashpw(user.password)
        return toAjax(userService.insertUser(user))
    }

    /**
     * 修改用户
     */
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody user: SysUserBo): R<Unit> {
        userService.checkUserAllowed(user.userId!!)
        userService.checkUserDataScope(user.userId!!)
        deptService.checkDeptDataScope(user.deptId!!)
        if (!userService.checkUserNameUnique(user)) {
            return fail("修改用户'" + user.userName + "'失败，登录账号已存在")
        } else if (StringUtils.isNotEmpty(user.phonenumber) && !userService.checkPhoneUnique(user)) {
            return fail("修改用户'" + user.userName + "'失败，手机号码已存在")
        } else if (StringUtils.isNotEmpty(user.email) && !userService.checkEmailUnique(user)) {
            return fail("修改用户'" + user.userName + "'失败，邮箱账号已存在")
        }
        return toAjax(userService.updateUser(user))
    }

    /**
     * 删除用户
     *
     * @param userIds 角色ID串
     */
    @SaCheckPermission("system:user:remove")
    @Log(title = "用户管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{userIds}")
    fun remove(@PathVariable userIds: Array<Long>): R<Unit> {
        return if (ArrayUtil.contains(userIds, getUserId())) {
            fail("当前用户不能删除")
        } else toAjax(userService.deleteUserByIds(userIds))
    }

    /**
     * 重置密码
     */
    @SaCheckPermission("system:user:resetPwd")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    fun resetPwd(@RequestBody user: SysUserBo): R<Unit> {
        userService.checkUserAllowed(user.userId!!)
        userService.checkUserDataScope(user.userId!!)
        user.password = BCrypt.hashpw(user.password)
        return toAjax(userService.resetUserPwd(user.userId!!, user.password!!))
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    fun changeStatus(@RequestBody user: SysUserBo): R<Unit> {
        userService.checkUserAllowed(user.userId!!)
        userService.checkUserDataScope(user.userId!!)
        return toAjax(userService.updateUserStatus(user.userId!!, user.status!!))
    }

    /**
     * 根据用户编号获取授权角色
     *
     * @param userId 用户ID
     */
    @SaCheckPermission("system:user:query")
    @GetMapping("/authRole/{userId}")
    fun authRole(@PathVariable userId: Long): R<SysUserInfoVo> {
        val user = userService.selectUserById(userId)
        val roles = roleService.selectRolesByUserId(userId)
        val userInfoVo = SysUserInfoVo()
        userInfoVo.user = user
        userInfoVo.roles = if (isSuperAdmin(userId)) roles else filter(roles!!) { r: SysRoleVo? -> !r!!.isSuperAdmin }
        return ok(userInfoVo)
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户Id
     * @param roleIds 角色ID串
     */
    @SaCheckPermission("system:user:edit")
    @Log(title = "用户管理", businessType = BusinessType.GRANT)
    @PutMapping("/authRole")
    fun insertAuthRole(userId: Long, roleIds: Array<Long>): R<Unit> {
        userService.checkUserDataScope(userId)
        userService.insertUserAuth(userId, roleIds)
        return ok()
    }

    /**
     * 获取部门树列表
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/deptTree")
    fun deptTree(dept: SysDeptBo): R<MutableList<Tree<Long>>> {
        return ok(deptService.selectDeptTreeList(dept))
    }

    /**
     * 获取部门下的所有用户信息
     */
    @SaCheckPermission("system:user:list")
    @GetMapping("/list/dept/{deptId}")
    fun listByDept(@PathVariable deptId: @NotNull Long): R<MutableList<SysUserVo>> {
        return ok(userService.selectUserListByDept(deptId))
    }
}
