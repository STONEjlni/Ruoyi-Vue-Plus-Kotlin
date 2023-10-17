package com.blank.system.service.impl

import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysNotice
import com.blank.system.domain.bo.SysNoticeBo
import com.blank.system.domain.vo.SysNoticeVo
import com.blank.system.mapper.SysNoticeMapper
import com.blank.system.mapper.SysUserMapper
import com.blank.system.service.ISysNoticeService
import com.mybatisflex.core.query.QueryWrapper
import org.springframework.stereotype.Service

/**
 * 公告 服务层实现
 */
@Service
class SysNoticeServiceImpl(
    private val baseMapper: SysNoticeMapper,
    private val userMapper: SysUserMapper
) : ISysNoticeService {

    override fun selectPageNoticeList(notice: SysNoticeBo, pageQuery: PageQuery): TableDataInfo<SysNoticeVo>? {
        /*LambdaQueryWrapper<SysNotice> lqw = buildQueryWrapper(notice);
        Page<SysNoticeVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 查询公告信息
     *
     * @param noticeId 公告ID
     * @return 公告信息
     */
    override fun selectNoticeById(noticeId: Long): SysNoticeVo? {
        return baseMapper.selectVoById(noticeId)!!
    }

    /**
     * 查询公告列表
     *
     * @param notice 公告信息
     * @return 公告集合
     */
    override fun selectNoticeList(notice: SysNoticeBo): MutableList<SysNoticeVo>? {
        /*LambdaQueryWrapper<SysNotice> lqw = buildQueryWrapper(notice);
        return baseMapper.selectVoList(lqw);*/
        return null
    }

    private fun  /*<SysNotice>*/buildQueryWrapper(bo: SysNoticeBo): QueryWrapper? {
        /*LambdaQueryWrapper<SysNotice> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getNoticeTitle()), SysNotice::getNoticeTitle, bo.getNoticeTitle());
        lqw.eq(StrUtil.isNotBlank(bo.getNoticeType()), SysNotice::getNoticeType, bo.getNoticeType());
        if (StrUtil.isNotBlank(bo.getCreateByName())) {
            SysUserVo sysUser = userMapper.selectUserByUserName(bo.getCreateByName());
            lqw.eq(SysNotice::getCreateBy, ObjectUtil.isNotNull(sysUser) ? sysUser.getUserId() : null);
        }
        lqw.orderByAsc(SysNotice::getNoticeId);
        return lqw;*/
        return null
    }

    /**
     * 新增公告
     *
     * @param bo 公告信息
     * @return 结果
     */
    override fun insertNotice(bo: SysNoticeBo): Int {
        val notice = convert(bo, SysNotice::class.java)!!
        return baseMapper!!.insert(notice)
    }

    /**
     * 修改公告
     *
     * @param bo 公告信息
     * @return 结果
     */
    override fun updateNotice(bo: SysNoticeBo): Int {
        /*SysNotice notice = MapstructUtils.convert(bo, SysNotice.class);
        return baseMapper.updateById(notice);*/
        return 0
    }

    /**
     * 删除公告对象
     *
     * @param noticeId 公告ID
     * @return 结果
     */
    override fun deleteNoticeById(noticeId: Long): Int {
        return baseMapper!!.deleteById(noticeId)
    }

    /**
     * 批量删除公告信息
     *
     * @param noticeIds 需要删除的公告ID
     * @return 结果
     */
    override fun deleteNoticeByIds(noticeIds: Array<Long>): Int {
        /*return baseMapper.deleteBatchIds(Arrays.asList(noticeIds));*/
        return 0
    }
}
