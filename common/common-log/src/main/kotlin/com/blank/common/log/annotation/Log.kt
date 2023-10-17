package com.blank.common.log.annotation

import com.blank.common.log.enums.BusinessType
import com.blank.common.log.enums.OperatorType


/**
 * 自定义操作日志记录注解
 */
@Target(AnnotationTarget.VALUE_PARAMETER, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Log(
    /**
     * 模块
     */
    val title: String = "",

    /**
     * 功能
     */
    val businessType: BusinessType = BusinessType.OTHER,

    /**
     * 操作人类别
     */
    val operatorType: OperatorType = OperatorType.MANAGE,

    /**
     * 是否保存请求的参数
     */
    val isSaveRequestData: Boolean = true,

    /**
     * 是否保存响应的参数
     */
    val isSaveResponseData: Boolean = true,


    /**
     * 排除指定的请求参数
     */
    val excludeParamNames: Array<String> = []

)
