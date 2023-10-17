package com.blank.common.json.handler

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl
import com.fasterxml.jackson.databind.ser.std.NumberSerializer
import java.io.IOException

/**
 * 超出 JS 最大最小值 处理
 */
@JacksonStdImpl
class BigNumberSerializer(
    rawType: Class<out Number>
) : NumberSerializer(rawType) {
    companion object {
        /**
         * 根据 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 得来
         */
        private const val MAX_SAFE_INTEGER = 9007199254740991L
        private const val MIN_SAFE_INTEGER = -9007199254740991L

        /**
         * 提供实例
         */
        val INSTANCE = BigNumberSerializer(Number::class.java)
    }

    @Throws(IOException::class)
    override fun serialize(value: Number, gen: JsonGenerator, provider: SerializerProvider?) {
        // 超出范围 序列化位字符串
        if (value.toLong() in (MIN_SAFE_INTEGER + 1)..<MAX_SAFE_INTEGER) {
            super.serialize(value, gen, provider)
        } else {
            gen.writeString(value.toString())
        }
    }

}
