package com.blank.common.sensitive.handler

import cn.hutool.core.util.ObjectUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.sensitive.annotation.Sensitive
import com.blank.common.sensitive.core.SensitiveService
import com.blank.common.sensitive.core.SensitiveStrategy
import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.BeanProperty
import com.fasterxml.jackson.databind.JsonMappingException
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.ContextualSerializer
import org.springframework.beans.BeansException
import java.io.IOException
import java.util.*

/**
 * 数据脱敏json序列化工具
 */
@Slf4j
class SensitiveHandler : JsonSerializer<String>(), ContextualSerializer {
    private var strategy: SensitiveStrategy? = null
    private var roleKey: String? = null
    private var perms: String? = null

    @Throws(IOException::class)
    override fun serialize(value: String, gen: JsonGenerator, serializers: SerializerProvider?) {
        try {
            val sensitiveService = SpringUtil.getBean(SensitiveService::class.java)
            if (ObjectUtil.isNotNull(sensitiveService) && sensitiveService.isSensitive(roleKey, perms)) {
                gen.writeString(strategy!!.desensitizer.apply(value))
            } else {
                gen.writeString(value)
            }
        } catch (e: BeansException) {
            log.error { "脱敏实现不存在, 采用默认处理 => ${e.message}" }
            gen.writeString(value)
        }
    }

    @Throws(JsonMappingException::class)
    override fun createContextual(prov: SerializerProvider, property: BeanProperty): JsonSerializer<*> {
        val annotation = property.getAnnotation(Sensitive::class.java)
        if (Objects.nonNull(annotation) && String::class.java == property.type.rawClass) {
            this.strategy = annotation.strategy
            this.roleKey = annotation.roleKey
            this.perms = annotation.perms
            return this
        }
        return prov.findValueSerializer(property.type, property)
    }
}
