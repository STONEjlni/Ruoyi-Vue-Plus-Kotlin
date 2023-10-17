package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.SysUserRole
import com.blank.system.domain.bo.SysDeptBo
import com.blank.system.domain.bo.SysRoleBo
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.domain.vo.DeptTreeSelectVo
import com.blank.system.domain.vo.SysRoleVo
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.service.ISysDeptService
import com.blank.system.service.ISysRoleService
import com.blank.system.service.ISysUserService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 角色信息
 */
@Validated
@RestController
@RequestMapping("/system/role")
class SysRoleController(
    private val roleService: ISysRoleService,
    private val userService: ISysUserService,
    private val deptService: ISysDeptService
) : BaseController() {

    /**
     * 获取角色信息列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/list")
    fun list(role: SysRoleBo, pageQuery: PageQuery): TableDataInfo<SysRoleVo>? {
        return roleService.selectPageRoleList(role, pageQuery)
    }

    /**
     * 根据角色编号获取详细信息
     *
     * @param roleId 角色ID
     */
    @SaCheckPermission("system:role:query")
    @GetMapping(value = ["/{roleId}"])
    fun getInfo(@PathVariable roleId: Long): R<SysRoleVo> {
        roleService.checkRoleDataScope(roleId)
        return ok(roleService.selectRoleById(roleId))
    }

    /**
     * 新增角色
     */
    @SaCheckPermission("system:role:add")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody role: SysRoleBo): R<Unit> {
        roleService.checkRoleAllowed(role)
        if (!roleService.checkRoleNameUnique(role)) {
            return fail("新增角色'" + role.roleName + "'失败，角色名称已存在")
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return fail("新增角色'" + role.roleName + "'失败，角色权限已存在")
        }
        return toAjax(roleService.insertRole(role))
    }

    /**
     * 修改保存角色
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody role: SysRoleBo): R<Unit> {
        roleService.checkRoleAllowed(role)
        roleService.checkRoleDataScope(role.roleId!!)
        if (!roleService.checkRoleNameUnique(role)) {
            return fail("修改角色'" + role.roleName + "'失败，角色名称已存在")
        } else if (!roleService.checkRoleKeyUnique(role)) {
            return fail("修改角色'" + role.roleName + "'失败，角色权限已存在")
        }
        if (roleService.updateRole(role) > 0) {
            roleService.cleanOnlineUserByRole(role.roleId!!)
            return ok()
        }
        return fail("修改角色'" + role.roleName + "'失败，请联系管理员")
    }

    /**
     * 修改保存数据权限
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/dataScope")
    fun dataScope(@RequestBody role: SysRoleBo): R<Unit> {
        roleService.checkRoleAllowed(role)
        roleService.checkRoleDataScope(role.roleId!!)
        return toAjax(roleService.authDataScope(role))
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    fun changeStatus(@RequestBody role: SysRoleBo): R<Unit> {
        roleService.checkRoleAllowed(role)
        roleService.checkRoleDataScope(role.roleId!!)
        return toAjax(roleService.updateRoleStatus(role.roleId!!, role.status!!))
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色ID串
     */
    @SaCheckPermission("system:role:remove")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    fun remove(@PathVariable roleIds: Array<Long>): R<Unit> {
        return toAjax(roleService.deleteRoleByIds(roleIds))
    }

    /**
     * 获取角色选择框列表
     */
    @SaCheckPermission("system:role:query")
    @GetMapping("/optionselect")
    fun optionselect(): R<MutableList<SysRoleVo>> {
        return ok(roleService.selectRoleAll())
    }

    /**
     * 查询已分配用户角色列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/allocatedList")
    fun allocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>? {
        return userService.selectAllocatedList(user, pageQuery)
    }

    /**
     * 查询未分配用户角色列表
     */
    @SaCheckPermission("system:role:list")
    @GetMapping("/authUser/unallocatedList")
    fun unallocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>? {
        return userService.selectUnallocatedList(user, pageQuery)
    }

    /**
     * 取消授权用户
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancel")
    fun cancelAuthUser(@RequestBody userRole: SysUserRole): R<Unit> {
        return toAjax(roleService.deleteAuthUser(userRole))
    }

    /**
     * 批量取消授权用户
     *
     * @param roleId  角色ID
     * @param userIds 用户ID串
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/cancelAll")
    fun cancelAuthUserAll(roleId: Long, userIds: Array<Long>): R<Unit> {
        return toAjax(roleService.deleteAuthUsers(roleId, userIds))
    }

    /**
     * 批量选择用户授权
     *
     * @param roleId  角色ID
     * @param userIds 用户ID串
     */
    @SaCheckPermission("system:role:edit")
    @Log(title = "角色管理", businessType = BusinessType.GRANT)
    @PutMapping("/authUser/selectAll")
    fun selectAuthUserAll(roleId: Long, userIds: Array<Long>): R<Unit> {
        roleService.checkRoleDataScope(roleId)
        return toAjax(roleService.insertAuthUsers(roleId, userIds))
    }

    /**
     * 获取对应角色部门树列表
     *
     * @param roleId 角色ID
     */
    @SaCheckPermission("system:role:list")
    @GetMapping(value = ["/deptTree/{roleId}"])
    fun roleDeptTreeselect(@PathVariable("roleId") roleId: Long): R<DeptTreeSelectVo> {
        val selectVo = DeptTreeSelectVo()
        selectVo.checkedKeys = deptService.selectDeptListByRoleId(roleId)
        selectVo.depts = deptService.selectDeptTreeList(SysDeptBo())
        return ok(selectVo)
    }
}
