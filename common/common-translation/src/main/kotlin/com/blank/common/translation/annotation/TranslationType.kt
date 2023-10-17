package com.blank.common.translation.annotation

import java.lang.annotation.Inherited


/**
 * 翻译类型注解 (标注到{@link TranslationInterface} 的实现类)
 */
@Inherited
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class TranslationType(
    /**
     * 类型
     */
    val type: String
)
