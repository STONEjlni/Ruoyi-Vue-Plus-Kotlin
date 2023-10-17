package com.blank.common.translation.annotation

import com.blank.common.translation.core.handler.TranslationHandler
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import java.lang.annotation.Inherited

/**
 * 通用翻译注解
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD, AnnotationTarget.FUNCTION)
@MustBeDocumented
@JacksonAnnotationsInside
@JsonSerialize(using = TranslationHandler::class)
annotation class Translation(
    /**
     * 类型 (需与实现类上的 [TranslationType] 注解type对应)
     *
     *
     * 默认取当前字段的值 如果设置了 @[Translation.mapper] 则取映射字段的值
     */
    val type: String,

    /**
     * 映射字段 (如果不为空则取此字段的值)
     */
    val mapper: String = "",

    /**
     * 其他条件 例如: 字典type(sys_user_sex)
     */
    val other: String = ""
)
