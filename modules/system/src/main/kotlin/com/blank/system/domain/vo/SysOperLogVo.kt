package com.blank.system.domain.vo

import com.blank.system.domain.SysOperLog
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 操作日志记录视图对象 sys_oper_log
 */
@AutoMapper(target = SysOperLog::class)
class SysOperLogVo : Serializable {
    /**
     * 日志主键
     */
    var operId: Long? = null

    /**
     * 模块标题
     */
    var title: String? = null

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    var businessType: Int? = null

    /**
     * 业务类型数组
     */
    var businessTypes: Array<Int>? = arrayOf()

    /**
     * 方法名称
     */
    var method: String? = null

    /**
     * 请求方式
     */
    var requestMethod: String? = null

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    var operatorType: Int? = null

    /**
     * 操作人员
     */
    var operName: String? = null

    /**
     * 部门名称
     */
    var deptName: String? = null

    /**
     * 请求URL
     */
    var operUrl: String? = null

    /**
     * 主机地址
     */
    var operIp: String? = null

    /**
     * 操作地点
     */
    var operLocation: String? = null

    /**
     * 请求参数
     */
    var operParam: String? = null

    /**
     * 返回参数
     */
    var jsonResult: String? = null

    /**
     * 操作状态（0正常 1异常）
     */
    var status: Int? = null

    /**
     * 错误消息
     */
    var errorMsg: String? = null

    /**
     * 操作时间
     */
    var operTime: Date? = null

    /**
     * 消耗时间
     */
    var costTime: Long? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
