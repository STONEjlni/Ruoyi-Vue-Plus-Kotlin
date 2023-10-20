package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysNoticeBo
import com.blank.system.domain.vo.SysNoticeVo

/**
 * 公告 服务层
 */
interface ISysNoticeService {
    fun selectPageNoticeList(notice: SysNoticeBo, pageQuery: PageQuery): TableDataInfo<SysNoticeVo>

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    fun selectNoticeById(noticeId: Long): SysNoticeVo?

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    fun selectNoticeList(notice: SysNoticeBo): MutableList<SysNoticeVo>

    /**
     * 新增公告
     *
     * @param bo 公告信息
     * @return 结果
     */
    fun insertNotice(bo: SysNoticeBo): Int

    /**
     * 修改公告
     *
     * @param bo 公告信息
     * @return 结果
     */
    fun updateNotice(bo: SysNoticeBo): Int

    /**
     * 删除公告信息
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    fun deleteNoticeById(noticeId: Long): Int

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    fun deleteNoticeByIds(noticeIds: Array<Long>): Int
}
