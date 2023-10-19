package com.blank.common.encrypt.interceptor

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.encrypt.annotation.EncryptField
import com.blank.common.encrypt.core.EncryptContext
import com.blank.common.encrypt.core.EncryptorManager
import com.blank.common.encrypt.enumd.AlgorithmType
import com.blank.common.encrypt.enumd.EncodeType
import com.blank.common.encrypt.properties.EncryptorProperties
import org.apache.ibatis.executor.parameter.ParameterHandler
import org.apache.ibatis.plugin.Interceptor
import org.apache.ibatis.plugin.Intercepts
import org.apache.ibatis.plugin.Invocation
import org.apache.ibatis.plugin.Signature
import java.lang.reflect.Field
import java.sql.PreparedStatement
import java.util.*

/**
 * 入参加密拦截器
 *
 */
@Slf4j
@Intercepts(Signature(type = ParameterHandler::class, method = "setParameters", args = [PreparedStatement::class]))
class MybatisEncryptInterceptor(
    private val encryptorManager: EncryptorManager,
    private val defaultProperties: EncryptorProperties
) : Interceptor {

    @Throws(Throwable::class)
    override fun intercept(invocation: Invocation): Any {
        return invocation
    }

    override fun plugin(target: Any): Any {
        if (target is ParameterHandler) {
            // 进行加密操作
            val parameterObject: Any = target.parameterObject
            if (ObjectUtil.isNotNull(parameterObject) && parameterObject !is String) {
                encryptHandler(parameterObject)
            }
        }
        return target
    }

    /**
     * 加密对象
     *
     * @param sourceObject 待加密对象
     */
    private fun encryptHandler(sourceObject: Any?) {
        if (ObjectUtil.isNull(sourceObject)) {
            return
        }
        if (sourceObject is Map<*, *>) {
            HashSet(sourceObject.values).forEach { encryptHandler(it!!) }
            return
        }
        if (sourceObject is List<*>) {
            if (CollUtil.isEmpty(sourceObject)) {
                return
            }
            // 判断第一个元素是否含有注解。如果没有直接返回，提高效率
            val firstItem: Any = sourceObject[0]!!
            if (ObjectUtil.isNull(firstItem) || CollUtil.isEmpty(encryptorManager.getFieldCache(firstItem.javaClass))) {
                return
            }
            sourceObject.forEach { encryptHandler(it!!) }
            return
        }
        val fields = encryptorManager.getFieldCache(sourceObject!!::class.java)
        try {
            for (field in fields) {
                field[sourceObject] = encryptField(Convert.toStr(field[sourceObject]), field)
            }
        } catch (e: Exception) {
            log.error(e) { "处理加密字段时出错" }
        }
    }

    /**
     * 字段值进行加密。通过字段的批注注册新的加密算法
     *
     * @param value 待加密的值
     * @param field 待加密字段
     * @return 加密后结果
     */
    private fun encryptField(value: String?, field: Field): String? {
        if (ObjectUtil.isNull(value)) {
            return null
        }
        val encryptField = field.getAnnotation(EncryptField::class.java)
        val encryptContext = EncryptContext()
        encryptContext.algorithm =
            if (encryptField.algorithm === AlgorithmType.DEFAULT) defaultProperties.algorithm else encryptField.algorithm
        encryptContext.encode =
            if (encryptField.encode === EncodeType.DEFAULT) defaultProperties.encode else encryptField.encode
        encryptContext.password =
            if (StrUtil.isBlank(encryptField.password)) defaultProperties.password else encryptField.password
        encryptContext.privateKey =
            if (StrUtil.isBlank(encryptField.privateKey)) defaultProperties.privateKey else encryptField.privateKey
        encryptContext.publicKey =
            if (StrUtil.isBlank(encryptField.publicKey)) defaultProperties.publicKey else encryptField.publicKey
        return encryptorManager.encrypt(value, encryptContext)
    }


    override fun setProperties(properties: Properties?) {}

}
