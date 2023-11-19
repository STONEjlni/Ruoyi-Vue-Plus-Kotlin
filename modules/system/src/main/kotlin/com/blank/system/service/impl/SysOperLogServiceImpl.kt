package com.blank.system.service.impl

import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.ip.AddressUtils.getRealAddressByIP
import com.blank.common.log.event.OperLogEvent
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysOperLog
import com.blank.system.domain.bo.SysOperLogBo
import com.blank.system.domain.table.SysOperLogDef.SYS_OPER_LOG
import com.blank.system.domain.vo.SysOperLogVo
import com.blank.system.mapper.SysOperLogMapper
import com.blank.system.service.ISysOperLogService
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.ArrayUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

/**
 * 操作日志 服务层处理
 */
@Service
class SysOperLogServiceImpl(
    private val baseMapper: SysOperLogMapper
) : ISysOperLogService {

    /**
     * 操作日志记录
     *
     * @param operLogEvent 操作日志事件
     */
    @Async
    @EventListener
    fun recordOper(operLogEvent: OperLogEvent) {
        val operLog: SysOperLogBo = convert(operLogEvent, SysOperLogBo::class.java)!!
        // 远程查询操作地点
        operLog.operLocation = getRealAddressByIP(operLog.operIp!!)
        insertOperlog(operLog)
    }

    override fun selectPageOperLogList(operLog: SysOperLogBo, pageQuery: PageQuery): TableDataInfo<SysOperLogVo> {
        val lqw = buildQueryWrapper(operLog)
        if (StringUtils.isBlank(pageQuery.orderByColumn)) {
            lqw.orderBy(SYS_OPER_LOG.OPER_ID, false)
        }
        val page = baseMapper.paginateAs(
            pageQuery.build(), lqw,
            SysOperLogVo::class.java
        )
        return TableDataInfo.build(page)
    }

    private fun buildQueryWrapper(operLog: SysOperLogBo): QueryWrapper {
        val params: MutableMap<String, Any> = operLog.params
        val queryWrapper: QueryWrapper = QueryWrapper.create().from(SYS_OPER_LOG)
            .where(SYS_OPER_LOG.TITLE.like(operLog.title))
            .and(SYS_OPER_LOG.OPER_IP.like(operLog.operIp))
            .and(
                SYS_OPER_LOG.BUSINESS_TYPE.eq(
                    operLog.businessType,
                    operLog.businessType != null && operLog.businessType!! > 0
                )
            )
            .and(SYS_OPER_LOG.STATUS.eq(operLog.status))
            .and(SYS_OPER_LOG.OPER_NAME.like(operLog.operName))
            .and(
                SYS_OPER_LOG.OPER_TIME.between(
                    params["beginTime"],
                    params["endTime"],
                    params["beginTime"] != null && params["endTime"] != null
                )
            )
        if (ArrayUtils.isNotEmpty(operLog.businessTypes)) {
            queryWrapper.and(SYS_OPER_LOG.BUSINESS_TYPE.`in`(listOf(operLog.businessTypes)))
        }
        return queryWrapper
    }

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    override fun insertOperlog(bo: SysOperLogBo) {
        val operLog: SysOperLog = convert(bo, SysOperLog::class.java)!!
        operLog.operTime = Date()
        baseMapper.insert(operLog, true)
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    override fun selectOperLogList(operLog: SysOperLogBo): MutableList<SysOperLogVo> {
        val queryWrapper = buildQueryWrapper(operLog)
        queryWrapper.orderBy(SYS_OPER_LOG.OPER_ID, false)
        return baseMapper.selectListByQueryAs(queryWrapper, SysOperLogVo::class.java)
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    override fun deleteOperLogByIds(operIds: Array<Long>): Int {
        return baseMapper.deleteBatchByIds(listOf(*operIds))
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    override fun selectOperLogById(operId: Long): SysOperLogVo {
        return baseMapper.selectOneWithRelationsByIdAs(operId, SysOperLogVo::class.java)
    }

    /**
     * 清空操作日志
     */
    override fun cleanOperLog() {
        // mybatis不允许不带where执行update delete
        baseMapper.deleteByQuery(QueryWrapper.create().from(SYS_OPER_LOG).where(SYS_OPER_LOG.OPER_ID.ne("-1")))
    }

}
