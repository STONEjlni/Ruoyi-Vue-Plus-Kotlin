package com.blank.system.domain.bo

import com.blank.system.domain.SysLogininfor
import io.github.linpeilie.annotations.AutoMapper
import java.util.*

/**
 * 系统访问记录业务对象 sys_logininfor
 */
@AutoMapper(target = SysLogininfor::class, reverseConvertGenerate = false)
class SysLogininforBo {
    /**
     * 访问ID
     */
    var infoId: Long? = null

    /**
     * 用户账号
     */
    var userName: String? = null

    /**
     * 客户端
     */
    var clientKey: String? = null

    /**
     * 设备类型
     */
    var deviceType: String? = null

    /**
     * 登录IP地址
     */
    var ipaddr: String? = null

    /**
     * 登录地点
     */
    var loginLocation: String? = null

    /**
     * 浏览器类型
     */
    var browser: String? = null

    /**
     * 操作系统
     */
    var os: String? = null

    /**
     * 登录状态（0成功 1失败）
     */
    var status: String? = null

    /**
     * 提示消息
     */
    var msg: String? = null

    /**
     * 访问时间
     */
    var loginTime: Date? = null

    /**
     * 请求参数
     */
    var params: Map<String, Any> = HashMap()
}
