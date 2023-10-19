package com.blank.common.translation.core.handler

import com.fasterxml.jackson.databind.BeanDescription
import com.fasterxml.jackson.databind.SerializationConfig
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter
import com.fasterxml.jackson.databind.ser.BeanSerializerModifier

/**
 * Bean 序列化修改器 解决 Null 被单独处理问题
 */
class TranslationBeanSerializerModifier : BeanSerializerModifier() {
    override fun changeProperties(
        config: SerializationConfig, beanDesc: BeanDescription,
        beanProperties: MutableList<BeanPropertyWriter>
    ): MutableList<BeanPropertyWriter> {
        for (writer in beanProperties) {
            // 如果序列化器为 TranslationHandler 的话 将 Null 值也交给他处理
            if (writer.serializer is TranslationHandler) {
                writer.assignNullSerializer(writer.serializer)
            }
        }
        return beanProperties
    }
}
