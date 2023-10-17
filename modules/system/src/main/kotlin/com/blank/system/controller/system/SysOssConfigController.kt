package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.validate.AddGroup
import com.blank.common.core.validate.EditGroup
import com.blank.common.core.validate.QueryGroup
import com.blank.common.idempotent.annotation.RepeatSubmit
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysOssConfigBo
import com.blank.system.domain.vo.SysOssConfigVo
import com.blank.system.service.ISysOssConfigService
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 对象存储配置
 */
@Validated
@RestController
@RequestMapping("/resource/oss/config")
class SysOssConfigController(
    private val ossConfigService: ISysOssConfigService
) : BaseController() {

    /**
     * 查询对象存储配置列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    fun list(
        @Validated(
            QueryGroup::class
        ) bo: SysOssConfigBo, pageQuery: PageQuery
    ): TableDataInfo<SysOssConfigVo>? {
        return ossConfigService.queryPageList(bo, pageQuery)
    }

    /**
     * 获取对象存储配置详细信息
     *
     * @param ossConfigId OSS配置ID
     */
    @SaCheckPermission("system:oss:query")
    @GetMapping("/{ossConfigId}")
    fun getInfo(@PathVariable ossConfigId: @NotNull(message = "主键不能为空") Long): R<SysOssConfigVo> {
        return ok(ossConfigService.queryById(ossConfigId))
    }

    /**
     * 新增对象存储配置
     */
    @SaCheckPermission("system:oss:add")
    @Log(title = "对象存储配置", businessType = BusinessType.INSERT)
    @RepeatSubmit
    @PostMapping
    fun add(
        @Validated(
            AddGroup::class
        ) @RequestBody bo: SysOssConfigBo
    ): R<Unit> {
        return toAjax(ossConfigService.insertByBo(bo))
    }

    /**
     * 修改对象存储配置
     */
    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit
    @PutMapping
    fun edit(
        @Validated(
            EditGroup::class
        ) @RequestBody bo: SysOssConfigBo
    ): R<Unit> {
        return toAjax(ossConfigService.updateByBo(bo))
    }

    /**
     * 删除对象存储配置
     *
     * @param ossConfigIds OSS配置ID串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "对象存储配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossConfigIds}")
    fun remove(@PathVariable ossConfigIds: Array<Long>): R<Unit> {
        return toAjax(ossConfigService.deleteWithValidByIds(ossConfigIds.toMutableList(), true))
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("system:oss:edit")
    @Log(title = "对象存储状态修改", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    fun changeStatus(@RequestBody bo: SysOssConfigBo): R<Unit> {
        return toAjax(ossConfigService.updateOssConfigStatus(bo))
    }
}
