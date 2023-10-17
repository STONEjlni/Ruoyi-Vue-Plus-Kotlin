package com.blank.system.controller.monitor

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.redis.utils.RedisUtils.deleteObject
import com.blank.common.redis.utils.RedisUtils.hasKey
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysLogininforBo
import com.blank.system.domain.vo.SysLogininforVo
import com.blank.system.service.ISysLogininforService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 系统访问记录
 */
@Validated
@RestController
@RequestMapping("/monitor/logininfor")
class SysLogininforController(
    private val logininforService: ISysLogininforService
) : BaseController() {


    /**
     * 获取系统访问记录列表
     */
    @SaCheckPermission("monitor:logininfor:list")
    @GetMapping("/list")
    fun list(logininfor: SysLogininforBo, pageQuery: PageQuery): TableDataInfo<SysLogininforVo>? {
        return logininforService.selectPageLogininforList(logininfor, pageQuery)
    }

    /**
     * 批量删除登录日志
     *
     * @param infoIds 日志ids
     */
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.DELETE)
    @DeleteMapping("/{infoIds}")
    fun remove(@PathVariable infoIds: Array<Long>): R<Unit> {
        return toAjax(logininforService.deleteLogininforByIds(infoIds))
    }

    /**
     * 清理系统访问记录
     */
    @SaCheckPermission("monitor:logininfor:remove")
    @Log(title = "登录日志", businessType = BusinessType.CLEAN)
    @DeleteMapping("/clean")
    fun clean(): R<Unit> {
        logininforService.cleanLogininfor()
        return ok()
    }

    @SaCheckPermission("monitor:logininfor:unlock")
    @Log(title = "账户解锁", businessType = BusinessType.OTHER)
    @GetMapping("/unlock/{userName}")
    fun unlock(@PathVariable("userName") userName: String): R<Unit> {
        val loginName = GlobalConstants.PWD_ERR_CNT_KEY + userName
        if (hasKey(loginName)) {
            deleteObject(loginName)
        }
        return ok()
    }
}
