package com.blank.system.service.impl

import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.ip.AddressUtils.getRealAddressByIP
import com.blank.common.log.event.OperLogEvent
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysOperLog
import com.blank.system.domain.bo.SysOperLogBo
import com.blank.system.domain.vo.SysOperLogVo
import com.blank.system.mapper.SysOperLogMapper
import com.blank.system.service.ISysOperLogService
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
        val operLog = convert<OperLogEvent, SysOperLogBo>(operLogEvent, SysOperLogBo::class.java)!!
        // 远程查询操作地点
        operLog.operLocation = getRealAddressByIP(operLog.operIp!!)
        insertOperlog(operLog)
    }

    override fun selectPageOperLogList(operLog: SysOperLogBo, pageQuery: PageQuery): TableDataInfo<SysOperLogVo>? {
        /*Map<String, Object> params = operLog.getParams();
        LambdaQueryWrapper<SysOperLog> lqw = new LambdaQueryWrapper<SysOperLog>()
            .like(StringUtils.isNotBlank(operLog.getOperIp()), SysOperLog::getOperIp, operLog.getOperIp())
            .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle())
            .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                SysOperLog::getBusinessType, operLog.getBusinessType())
            .func(f -> {
                if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                    f.in(SysOperLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                }
            })
            .eq(operLog.getStatus() != null,
                SysOperLog::getStatus, operLog.getStatus())
            .like(StrUtil.isNotBlank(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysOperLog::getOperTime, params.get("beginTime"), params.get("endTime"));
        if (StrUtil.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("oper_id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysOperLogVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 新增操作日志
     *
     * @param bo 操作日志对象
     */
    override fun insertOperlog(bo: SysOperLogBo) {
        val operLog = convert(bo, SysOperLog::class.java)!!
        operLog.operTime = Date()
        baseMapper.insert(operLog)
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    override fun selectOperLogList(operLog: SysOperLogBo): MutableList<SysOperLogVo>? {
        /*Map<String, Object> params = operLog.getParams();
        return baseMapper.selectVoList(new LambdaQueryWrapper<SysOperLog>()
            .like(StringUtils.isNotBlank(operLog.getOperIp()), SysOperLog::getOperIp, operLog.getOperIp())
            .like(StringUtils.isNotBlank(operLog.getTitle()), SysOperLog::getTitle, operLog.getTitle())
            .eq(operLog.getBusinessType() != null && operLog.getBusinessType() > 0,
                SysOperLog::getBusinessType, operLog.getBusinessType())
            .func(f -> {
                if (ArrayUtil.isNotEmpty(operLog.getBusinessTypes())) {
                    f.in(SysOperLog::getBusinessType, Arrays.asList(operLog.getBusinessTypes()));
                }
            })
            .eq(operLog.getStatus() != null && operLog.getStatus() > 0,
                SysOperLog::getStatus, operLog.getStatus())
            .like(StrUtil.isNotBlank(operLog.getOperName()), SysOperLog::getOperName, operLog.getOperName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysOperLog::getOperTime, params.get("beginTime"), params.get("endTime"))
            .orderByDesc(SysOperLog::getOperId));*/
        return null
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    override fun deleteOperLogByIds(operIds: Array<Long>): Int {
        /*return baseMapper.deleteBatchIds(Arrays.asList(operIds));*/
        return 0
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    override fun selectOperLogById(operId: Long): SysOperLogVo? {
        return baseMapper.selectVoById(operId)!!
    }

    /**
     * 清空操作日志
     */
    override fun cleanOperLog() {
        /*baseMapper.delete(new LambdaQueryWrapper<>());*/
    }
}
