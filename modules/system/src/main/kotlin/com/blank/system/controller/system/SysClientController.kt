package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.validate.AddGroup
import com.blank.common.core.validate.EditGroup
import com.blank.common.idempotent.annotation.RepeatSubmit
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysClientBo
import com.blank.system.domain.vo.SysClientVo
import com.blank.system.service.ISysClientService
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 客户端管理
 */
@Validated
@RestController
@RequestMapping("/system/client")
class SysClientController(
    private val sysClientService: ISysClientService
) : BaseController() {

    /**
     * 查询客户端管理列表
     */
    @SaCheckPermission("system:client:list")
    @GetMapping("/list")
    fun list(bo: SysClientBo, pageQuery: PageQuery): TableDataInfo<SysClientVo>? {
        return sysClientService.queryPageList(bo, pageQuery)
    }

    /**
     * 获取客户端管理详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission("system:client:query")
    @GetMapping("/{id}")
    fun getInfo(@PathVariable id: @NotNull(message = "主键不能为空") Long): R<SysClientVo> {
        return ok(sysClientService.queryById(id))
    }

    /**
     * 新增客户端管理
     */
    @SaCheckPermission("system:client:add")
    @Log(title = "客户端管理", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    fun add(
        @Validated(
            AddGroup::class
        ) @RequestBody bo: SysClientBo
    ): R<Unit> {
        return toAjax(sysClientService.insertByBo(bo))
    }

    /**
     * 修改客户端管理
     */
    @SaCheckPermission("system:client:edit")
    @Log(title = "客户端管理", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    fun edit(
        @Validated(
            EditGroup::class
        ) @RequestBody bo: SysClientBo
    ): R<Unit> {
        return toAjax(sysClientService.updateByBo(bo))
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:client:edit")
    @Log(title = "客户端管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    fun changeStatus(@RequestBody bo: SysClientBo): R<Unit> {
        return toAjax(sysClientService.updateUserStatus(bo.id!!, bo.status!!))
    }

    /**
     * 删除客户端管理
     *
     * @param ids 主键串
     */
    @SaCheckPermission("system:client:remove")
    @Log(title = "客户端管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    fun remove(@PathVariable ids: Array<Long>): R<Unit> {
        return toAjax(sysClientService.deleteWithValidByIds(ids.toMutableList(), true))
    }
}
