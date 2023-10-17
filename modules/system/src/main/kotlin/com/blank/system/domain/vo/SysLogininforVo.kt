package com.blank.system.domain.vo

import com.blank.system.domain.SysLogininfor
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 系统访问记录视图对象 sys_logininfor
 */
@AutoMapper(target = SysLogininfor::class)
class SysLogininforVo : Serializable {
    /**
     * 访问ID
     */
    var infoId: Long? = null

    /**
     * 用户账号
     */
    var userName: String? = null

    /**
     * 登录状态（0成功 1失败）
     */
    var status: String? = null

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
     * 提示消息
     */
    var msg: String? = null

    /**
     * 访问时间
     */
    var loginTime: Date? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
