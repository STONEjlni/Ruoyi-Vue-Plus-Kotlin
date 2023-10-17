package com.blank.system.service.impl

import cn.hutool.extra.servlet.JakartaServletUtil
import cn.hutool.http.useragent.UserAgentUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.constant.Constants
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.ip.AddressUtils.getRealAddressByIP
import com.blank.common.log.event.LogininforEvent
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysLogininfor
import com.blank.system.domain.bo.SysLogininforBo
import com.blank.system.domain.vo.SysLogininforVo
import com.blank.system.mapper.SysLogininforMapper
import com.blank.system.service.ISysLogininforService
import org.apache.commons.lang3.StringUtils
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Service
import java.util.*

/**
 * 系统访问日志情况信息 服务层处理
 */
@Slf4j
@Service
class SysLogininforServiceImpl(
    private val baseMapper: SysLogininforMapper
) : ISysLogininforService {


    /**
     * 记录登录信息
     *
     * @param logininforEvent 登录事件
     */
    @Async
    @EventListener
    fun recordLogininfor(logininforEvent: LogininforEvent) {
        val request = logininforEvent.request
        val userAgent = UserAgentUtil.parse(request!!.getHeader("User-Agent"))
        val ip = JakartaServletUtil.getClientIP(request)
        val address = getRealAddressByIP(ip)
        val s = StringBuilder()
        s.append(getBlock(ip))
        s.append(address)
        s.append(getBlock(logininforEvent.username))
        s.append(getBlock(logininforEvent.status))
        s.append(getBlock(logininforEvent.message))
        // 打印信息到日志
        log.info { "${s.toString()} ${logininforEvent.args}" }
        // 获取客户端操作系统
        val os = userAgent.os.name
        // 获取客户端浏览器
        val browser = userAgent.browser.name
        // 封装对象
        val logininfor = SysLogininforBo()
        logininfor.userName = logininforEvent.username
        logininfor.ipaddr = ip
        logininfor.loginLocation = address
        logininfor.browser = browser
        logininfor.os = os
        logininfor.msg = logininforEvent.message
        // 日志状态
        if (StringUtils.equalsAny(
                logininforEvent.status,
                Constants.LOGIN_SUCCESS,
                Constants.LOGOUT,
                Constants.REGISTER
            )
        ) {
            logininfor.status = Constants.SUCCESS
        } else if (Constants.LOGIN_FAIL == logininforEvent.status) {
            logininfor.status = Constants.FAIL
        }
        // 插入数据
        insertLogininfor(logininfor)
    }

    private fun getBlock(msg: Any?): String {
        var msg = msg
        if (msg == null) {
            msg = ""
        }
        return "[$msg]"
    }

    override fun selectPageLogininforList(
        logininfor: SysLogininforBo,
        pageQuery: PageQuery
    ): TableDataInfo<SysLogininforVo>? {
        /*Map<String, Object> params = logininfor.getParams();
        LambdaQueryWrapper<SysLogininfor> lqw = new LambdaQueryWrapper<SysLogininfor>()
            .like(StrUtil.isNotBlank(logininfor.getIpaddr()), SysLogininfor::getIpaddr, logininfor.getIpaddr())
            .eq(StrUtil.isNotBlank(logininfor.getStatus()), SysLogininfor::getStatus, logininfor.getStatus())
            .like(StrUtil.isNotBlank(logininfor.getUserName()), SysLogininfor::getUserName, logininfor.getUserName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysLogininfor::getLoginTime, params.get("beginTime"), params.get("endTime"));
        if (StrUtil.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("info_id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysLogininforVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 新增系统登录日志
     *
     * @param bo 访问日志对象
     */
    override fun insertLogininfor(bo: SysLogininforBo) {
        val logininfor = convert(bo, SysLogininfor::class.java)!!
        logininfor.loginTime = Date()
        baseMapper.insert(logininfor)
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    override fun selectLogininforList(logininfor: SysLogininforBo): MutableList<SysLogininforVo>? {
        /*Map<String, Object> params = logininfor.getParams();
        return baseMapper.selectVoList(new LambdaQueryWrapper<SysLogininfor>()
            .like(StrUtil.isNotBlank(logininfor.getIpaddr()), SysLogininfor::getIpaddr, logininfor.getIpaddr())
            .eq(StrUtil.isNotBlank(logininfor.getStatus()), SysLogininfor::getStatus, logininfor.getStatus())
            .like(StrUtil.isNotBlank(logininfor.getUserName()), SysLogininfor::getUserName, logininfor.getUserName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysLogininfor::getLoginTime, params.get("beginTime"), params.get("endTime"))
            .orderByDesc(SysLogininfor::getInfoId));*/
        return null
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    override fun deleteLogininforByIds(infoIds: Array<Long>): Int {
        /*return baseMapper.deleteBatchIds(Arrays.asList(infoIds));*/
        return 0
    }

    /**
     * 清空系统登录日志
     */
    override fun cleanLogininfor() {
        /*baseMapper.delete(new LambdaQueryWrapper<>());*/
    }
}
