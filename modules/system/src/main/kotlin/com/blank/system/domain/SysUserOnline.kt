package com.blank.system.domain

/**
 * 当前在线会话
 */
class SysUserOnline {
    /**
     * 会话编号
     */
    var tokenId: String? = null

    /**
     * 部门名称
     */
    var deptName: String? = null

    /**
     * 用户名称
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
     * 登录地址
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
     * 登录时间
     */
    var loginTime: Long? = null
}
