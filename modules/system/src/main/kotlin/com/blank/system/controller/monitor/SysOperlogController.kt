package com.blank.system.controller.monitor

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysOperLogBo
import com.blank.system.domain.vo.SysOperLogVo
import com.blank.system.service.ISysOperLogService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 操作日志记录
 */
@Validated
@RestController
@RequestMapping("/monitor/operlog")
class SysOperlogController(
    private val operLogService: ISysOperLogService
) : BaseController() {


    /**
     * 获取操作日志记录列表
     */
    @SaCheckPermission("monitor:operlog:list")
    @GetMapping("/list")
    fun list(operLog: SysOperLogBo, pageQuery: PageQuery): TableDataInfo<SysOperLogVo>? {
        return operLogService.selectPageOperLogList(operLog, pageQuery)
    }

    /**
     * 批量删除操作日志记录
     *
     * @param operIds 日志ids
     */
    @Log(title = "操作日志", businessType = BusinessType.DELETE)
    @SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/{operIds}")
    fun remove(@PathVariable operIds: Array<Long>): R<Unit> {
        return toAjax(operLogService.deleteOperLogByIds(operIds))
    }

    /**
     * 清理操作日志记录
     */
    @Log(title = "操作日志", businessType = BusinessType.CLEAN)
    @SaCheckPermission("monitor:operlog:remove")
    @DeleteMapping("/clean")
    fun clean(): R<Unit> {
        operLogService.cleanOperLog()
        return ok()
    }
}
