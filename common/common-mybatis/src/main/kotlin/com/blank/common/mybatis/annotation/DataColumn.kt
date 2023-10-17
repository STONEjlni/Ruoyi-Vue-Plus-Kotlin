package com.blank.common.mybatis.annotation


/**
 * 数据权限
 * <p>
 * 一个注解只能对应一个模板
 *
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DataColumn(
    /**
     * 占位符关键字
     */
    val key: Array<String> = ["deptName"],

    /**
     * 占位符替换值
     */
    val value: Array<String> = ["dept_id"]
)
