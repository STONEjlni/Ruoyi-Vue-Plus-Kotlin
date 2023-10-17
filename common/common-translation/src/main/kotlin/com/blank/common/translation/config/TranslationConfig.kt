package com.blank.common.translation.config

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.translation.annotation.TranslationType
import com.blank.common.translation.core.TranslationInterface
import com.blank.common.translation.core.handler.TranslationBeanSerializerModifier
import com.blank.common.translation.core.handler.TranslationHandler
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.boot.autoconfigure.AutoConfiguration

/**
 * 翻译模块配置类
 */
@Slf4j
@AutoConfiguration
class TranslationConfig(
    var list: List<TranslationInterface<*>>,
    val objectMapper: ObjectMapper
) {
    @PostConstruct
    fun init() {
        val map: MutableMap<String, TranslationInterface<*>> = HashMap(list.size)
        for (trans in list) {
            if (trans.javaClass.isAnnotationPresent(TranslationType::class.java)) {
                val annotation = trans.javaClass.getAnnotation(TranslationType::class.java)
                map[annotation.type] = trans
            } else {
                log.warn { "${trans.javaClass.getName()} 翻译实现类未标注 TranslationType 注解!" }
            }
        }
        TranslationHandler.TRANSLATION_MAPPER.putAll(map)
        // 设置 Bean 序列化修改器
        objectMapper.setSerializerFactory(
            objectMapper.serializerFactory
                .withSerializerModifier(TranslationBeanSerializerModifier())
        )
    }
}
