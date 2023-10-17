package com.blank.common.mybatis.annotation

/**
 * 数据权限
 * <p>
 * 一个注解只能对应一个模板
 *
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class DataPermission(
    val value: Array<DataColumn> = []
)
