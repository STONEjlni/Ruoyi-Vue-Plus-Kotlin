package com.blank.common.encrypt.core

import cn.hutool.core.util.ReflectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.encrypt.annotation.EncryptField
import java.lang.reflect.Field
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.stream.Collectors

/**
 * 加密管理类
 *
 */
@Slf4j
class EncryptorManager {
    /**
     * 缓存加密器
     */
    private var encryptorMap: MutableMap<EncryptContext, IEncryptor> = ConcurrentHashMap()

    /**
     * 类加密字段缓存
     */
    private var fieldCache: MutableMap<Class<*>, Set<Field>> = ConcurrentHashMap()

    /**
     * 获取类加密字段缓存
     */
    fun getFieldCache(sourceClazz: Class<*>): Set<Field> {
        return fieldCache.computeIfAbsent(
            sourceClazz
        ) { clazz: Class<*>? ->
            var clazz = clazz
            var fieldSet: MutableSet<Field> =
                HashSet()
            while (clazz != null) {
                val fields = clazz.getDeclaredFields()
                fieldSet.addAll(Arrays.asList(*fields))
                clazz = clazz.superclass
            }
            fieldSet = fieldSet.stream()
                .filter { field: Field ->
                    field.isAnnotationPresent(
                        EncryptField::class.java
                    ) && field.type == String::class.java
                }
                .collect(Collectors.toSet())
            for (field in fieldSet) {
                field.setAccessible(true)
            }
            fieldSet
        }
    }

    /**
     * 注册加密执行者到缓存
     *
     * @param encryptContext 加密执行者需要的相关配置参数
     */
    fun registAndGetEncryptor(encryptContext: EncryptContext): IEncryptor? {
        if (encryptorMap.containsKey(encryptContext)) {
            return encryptorMap[encryptContext]
        }
        val encryptor: IEncryptor = ReflectUtil.newInstance(encryptContext.algorithm?.clazz, encryptContext)
        encryptorMap[encryptContext] = encryptor
        return encryptor
    }

    /**
     * 移除缓存中的加密执行者
     *
     * @param encryptContext 加密执行者需要的相关配置参数
     */
    fun removeEncryptor(encryptContext: EncryptContext) {
        encryptorMap.remove(encryptContext)
    }

    /**
     * 根据配置进行加密。会进行本地缓存对应的算法和对应的秘钥信息。
     *
     * @param value          待加密的值
     * @param encryptContext 加密相关的配置信息
     */
    fun encrypt(value: String?, encryptContext: EncryptContext): String? {
        val encryptor = registAndGetEncryptor(encryptContext)
        return encryptor!!.encrypt(value, encryptContext.encode)
    }

    /**
     * 根据配置进行解密
     *
     * @param value          待解密的值
     * @param encryptContext 加密相关的配置信息
     */
    fun decrypt(value: String?, encryptContext: EncryptContext): String? {
        val encryptor = registAndGetEncryptor(encryptContext)
        return encryptor!!.decrypt(value)
    }
}
