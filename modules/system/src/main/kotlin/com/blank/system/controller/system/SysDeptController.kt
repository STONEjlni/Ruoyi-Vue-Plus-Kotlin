package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.hutool.core.convert.Convert
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.domain.R.Companion.warn
import com.blank.common.core.utils.StringUtilsExtend.splitList
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysDeptBo
import com.blank.system.domain.vo.SysDeptVo
import com.blank.system.service.ISysDeptService
import org.apache.commons.lang3.StringUtils
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 部门信息
 */
@Validated
@RestController
@RequestMapping("/system/dept")
class SysDeptController(
    private val deptService: ISysDeptService
) : BaseController() {

    /**
     * 获取部门列表
     */
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list")
    fun list(dept: SysDeptBo): R<MutableList<SysDeptVo>> {
        val depts = deptService.selectDeptList(dept)
        return ok(data = depts)
    }

    /**
     * 查询部门列表（排除节点）
     *
     * @param deptId 部门ID
     */
    @SaCheckPermission("system:dept:list")
    @GetMapping("/list/exclude/{deptId}")
    fun excludeChild(@PathVariable(value = "deptId", required = false) deptId: Long): R<MutableList<SysDeptVo>> {
        val depts = deptService.selectDeptList(SysDeptBo())
        depts.removeIf { d: SysDeptVo? ->
            d!!.deptId == deptId || splitList(d.ancestors!!).contains(
                Convert.toStr(
                    deptId
                )
            )
        }
        return ok(data = depts)
    }

    /**
     * 根据部门编号获取详细信息
     *
     * @param deptId 部门ID
     */
    @SaCheckPermission("system:dept:query")
    @GetMapping(value = ["/{deptId}"])
    fun getInfo(@PathVariable deptId: Long): R<SysDeptVo> {
        deptService.checkDeptDataScope(deptId)
        return ok(data = deptService.selectDeptById(deptId))
    }

    /**
     * 新增部门
     */
    @SaCheckPermission("system:dept:add")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody dept: SysDeptBo): R<Unit> {
        return if (!deptService.checkDeptNameUnique(dept)) {
            fail(msg = "新增部门'${dept.deptName}'失败，部门名称已存在")
        } else toAjax(deptService.insertDept(dept))
    }

    /**
     * 修改部门
     */
    @SaCheckPermission("system:dept:edit")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody dept: SysDeptBo): R<Unit> {
        val deptId = dept.deptId!!
        deptService.checkDeptDataScope(deptId)
        if (!deptService.checkDeptNameUnique(dept)) {
            return fail(msg = "修改部门'${dept.deptName}'失败，部门名称已存在")
        } else if (dept.parentId == deptId) {
            return fail(msg = "修改部门'${dept.deptName}'失败，上级部门不能是自己")
        } else if (StringUtils.equals(UserConstants.DEPT_DISABLE, dept.status)) {
            if (deptService.selectNormalChildrenDeptById(deptId) > 0) {
                return fail(msg = "该部门包含未停用的子部门!")
            } else if (deptService.checkDeptExistUser(deptId)) {
                return fail(msg = "该部门下存在已分配用户，不能禁用!")
            }
        }
        return toAjax(deptService.updateDept(dept))
    }

    /**
     * 删除部门
     *
     * @param deptId 部门ID
     */
    @SaCheckPermission("system:dept:remove")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{deptId}")
    fun remove(@PathVariable deptId: Long): R<Unit> {
        if (deptService.hasChildByDeptId(deptId)) {
            return warn(msg = "存在下级部门,不允许删除")
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return warn(msg = "部门存在用户,不允许删除")
        }
        deptService.checkDeptDataScope(deptId)
        return toAjax(deptService.deleteDeptById(deptId))
    }
}
