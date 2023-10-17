package com.blank.common.json.utils

import cn.hutool.core.lang.Dict
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import org.apache.commons.lang3.StringUtils
import java.io.IOException

/**
 * JSON 工具类
 */
object JsonUtils {

    private val OBJECT_MAPPER = SpringUtil.getBean(ObjectMapper::class.java)

    @JvmStatic
    fun getObjectMapper(): ObjectMapper {
        return OBJECT_MAPPER
    }

    @JvmStatic
    fun toJsonString(`object`: Any?): String? {
        return if (ObjectUtil.isNull(`object`)) {
            null
        } else try {
            OBJECT_MAPPER.writeValueAsString(`object`)
        } catch (e: JsonProcessingException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun <T> parseObject(text: String?, clazz: Class<T>?): T? {
        return if (StringUtils.isEmpty(text)) {
            null
        } else try {
            OBJECT_MAPPER.readValue(text, clazz)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun <T> parseObject(bytes: ByteArray?, clazz: Class<T>?): T? {
        return if (ArrayUtil.isEmpty(bytes)) {
            null
        } else try {
            OBJECT_MAPPER.readValue(bytes, clazz)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun <T> parseObject(text: String?, typeReference: TypeReference<T>?): T? {
        return if (StrUtil.isBlank(text)) {
            null
        } else try {
            OBJECT_MAPPER.readValue(text, typeReference)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun parseMap(text: String?): Dict? {
        return if (StrUtil.isBlank(text)) {
            null
        } else try {
            OBJECT_MAPPER.readValue<Dict>(text, OBJECT_MAPPER.typeFactory.constructType(Dict::class.java))
        } catch (e: MismatchedInputException) {
            // 类型不匹配说明不是json
            null
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun parseArrayMap(text: String?): List<Dict>? {
        return if (StrUtil.isBlank(text)) {
            null
        } else try {
            OBJECT_MAPPER.readValue<List<Dict>>(
                text, OBJECT_MAPPER.typeFactory.constructCollectionType(
                    MutableList::class.java,
                    Dict::class.java
                )
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @JvmStatic
    fun <T> parseArray(text: String?, clazz: Class<T>?): List<T> {
        return if (StringUtils.isEmpty(text)) {
            ArrayList()
        } else try {
            OBJECT_MAPPER.readValue<List<T>>(
                text, OBJECT_MAPPER.typeFactory.constructCollectionType(MutableList::class.java, clazz)
            )
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }
}
