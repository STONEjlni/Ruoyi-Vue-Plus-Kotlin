package com.blank.system.service.impl

import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.utils.MapstructUtils
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysNotice
import com.blank.system.domain.bo.SysNoticeBo
import com.blank.system.domain.table.SysNoticeDef.SYS_NOTICE
import com.blank.system.domain.vo.SysNoticeVo
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysNoticeMapper
import com.blank.system.mapper.SysUserMapper
import com.blank.system.service.ISysNoticeService
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

/**
 * 公告 服务层实现
 */
@Service
class SysNoticeServiceImpl(
    private val baseMapper: SysNoticeMapper,
    private val userMapper: SysUserMapper
) : ISysNoticeService {

    override fun selectPageNoticeList(notice: SysNoticeBo, pageQuery: PageQuery): TableDataInfo<SysNoticeVo> {
        val lqw = buildQueryWrapper(notice)
        val page = baseMapper.paginateAs(pageQuery, lqw, SysNoticeVo::class.java)
        return TableDataInfo.build(page)
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    override fun selectNoticeById(noticeId: Long): SysNoticeVo {
        return baseMapper.selectOneWithRelationsByIdAs(noticeId, SysNoticeVo::class.java)
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    override fun selectNoticeList(notice: SysNoticeBo): MutableList<SysNoticeVo> {
        val lqw = buildQueryWrapper(notice)
        return baseMapper.selectListByQueryAs(lqw, SysNoticeVo::class.java)
    }

    private fun buildQueryWrapper(bo: SysNoticeBo): QueryWrapper {
        val queryWrapper: QueryWrapper = QueryWrapper.create().from(SYS_NOTICE)
            .where(SYS_NOTICE.NOTICE_TITLE.like(bo.noticeTitle))
            .and(SYS_NOTICE.NOTICE_TYPE.eq(bo.noticeType))
        if (StringUtils.isNotBlank(bo.createByName)) {
            val sysUser: SysUserVo = userMapper.selectUserByUserName(bo.createByName!!)
            queryWrapper.and(SYS_NOTICE.CREATE_BY.eq(if (ObjectUtil.isNotNull(sysUser)) sysUser.userId else null))
        }
        queryWrapper.orderBy(SYS_NOTICE.NOTICE_ID, true)
        return queryWrapper
    }

    /**
     * 新增公告
     *
     * @param bo 公告信息
     * @return 结果
     */
    override fun insertNotice(bo: SysNoticeBo): Int {
        val notice: SysNotice? = MapstructUtils.convert(bo, SysNotice::class.java)
        return baseMapper.insert(notice, true)
    }

    /**
     * 修改公告
     *
     * @param bo 公告信息
     * @return 结果
     */
    override fun updateNotice(bo: SysNoticeBo): Int {
        val notice: SysNotice? = MapstructUtils.convert(bo, SysNotice::class.java)
        return baseMapper.update(notice)
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    override fun deleteNoticeById(noticeId: Long): Int {
        return baseMapper.deleteById(noticeId)
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    override fun deleteNoticeByIds(noticeIds: Array<Long>): Int {
        return baseMapper.deleteBatchByIds(listOf(*noticeIds))
    }
}
