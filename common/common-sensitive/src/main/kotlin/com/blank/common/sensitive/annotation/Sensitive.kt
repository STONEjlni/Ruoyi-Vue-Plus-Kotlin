package com.blank.common.sensitive.annotation

import com.blank.common.sensitive.core.SensitiveStrategy
import com.blank.common.sensitive.handler.SensitiveHandler
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside
import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * 数据脱敏注解
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FIELD)
@JacksonAnnotationsInside
@JsonSerialize(using = SensitiveHandler::class)
annotation class Sensitive(
    val strategy: SensitiveStrategy
)
