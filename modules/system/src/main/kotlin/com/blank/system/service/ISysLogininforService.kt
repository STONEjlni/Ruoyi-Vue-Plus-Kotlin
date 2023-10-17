package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysLogininforBo
import com.blank.system.domain.vo.SysLogininforVo

/**
 * 系统访问日志情况信息 服务层
 */
interface ISysLogininforService {
    fun selectPageLogininforList(logininfor: SysLogininforBo, pageQuery: PageQuery): TableDataInfo<SysLogininforVo>?

    /**
     * 新增系统登录日志
     *
     * @param bo 访问日志对象
     */
    fun insertLogininfor(bo: SysLogininforBo)

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    fun selectLogininforList(logininfor: SysLogininforBo): MutableList<SysLogininforVo>?

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    fun deleteLogininforByIds(infoIds: Array<Long>): Int

    /**
     * 清空系统登录日志
     */
    fun cleanLogininfor()
}
