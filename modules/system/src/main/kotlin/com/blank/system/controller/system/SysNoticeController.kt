package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.service.DictService
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.common.websocket.utils.WebSocketUtils
import com.blank.system.domain.bo.SysNoticeBo
import com.blank.system.domain.vo.SysNoticeVo
import com.blank.system.service.ISysNoticeService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*


/**
 * 公告 信息操作处理
 */
@Validated
@RestController
@RequestMapping("/system/notice")
class SysNoticeController(
    private val noticeService: ISysNoticeService,
    private val dictService: DictService
) : BaseController() {

    /**
     * 获取通知公告列表
     */
    @SaCheckPermission("system:notice:list")
    @GetMapping("/list")
    fun list(notice: SysNoticeBo, pageQuery: PageQuery): TableDataInfo<SysNoticeVo>? {
        return noticeService.selectPageNoticeList(notice, pageQuery)
    }

    /**
     * 根据通知公告编号获取详细信息
     *
     * @param noticeId 公告ID
     */
    @SaCheckPermission("system:notice:query")
    @GetMapping(value = ["/{noticeId}"])
    fun getInfo(@PathVariable noticeId: Long): R<SysNoticeVo> {
        return ok(data = noticeService.selectNoticeById(noticeId))
    }

    /**
     * 新增通知公告
     */
    @SaCheckPermission("system:notice:add")
    @Log(title = "通知公告", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody notice: SysNoticeBo): R<Unit> {
        val rows = noticeService.insertNotice(notice)
        if (rows <= 0) {
            return R.fail()
        }
        val type = dictService.getDictLabel("sys_notice_type", notice.noticeType!!)
        WebSocketUtils.publishAll("[" + type + "] ${notice.noticeTitle}")
        return ok()
    }

    /**
     * 修改通知公告
     */
    @SaCheckPermission("system:notice:edit")
    @Log(title = "通知公告", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody notice: SysNoticeBo): R<Unit> {
        return toAjax(noticeService.updateNotice(notice))
    }

    /**
     * 删除通知公告
     *
     * @param noticeIds 公告ID串
     */
    @SaCheckPermission("system:notice:remove")
    @Log(title = "通知公告", businessType = BusinessType.DELETE)
    @DeleteMapping("/{noticeIds}")
    fun remove(@PathVariable noticeIds: Array<Long>): R<Unit> {
        return toAjax(noticeService.deleteNoticeByIds(noticeIds))
    }
}
