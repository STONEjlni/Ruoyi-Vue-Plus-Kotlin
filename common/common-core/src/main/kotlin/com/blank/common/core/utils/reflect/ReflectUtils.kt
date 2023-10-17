package com.blank.common.core.utils.reflect

import cn.hutool.core.util.ReflectUtil
import org.apache.commons.lang3.StringUtils

/**
 * 反射工具类. 提供调用getter/setter方法, 访问私有变量, 调用私有方法, 获取泛型类型Class, 被AOP过的真实类等工具函数.
 */
object ReflectUtils {

    private const val SETTER_PREFIX = "set"

    private const val GETTER_PREFIX = "get"

    /**
     * 调用Getter方法.
     * 支持多级，如：对象名.对象名.方法
     */
    @JvmStatic
    fun <E> invokeGetter(obj: Any, propertyName: String): E {
        var `object` = obj
        for (name in StringUtils.split(propertyName, ".")) {
            val getterMethodName = GETTER_PREFIX + StringUtils.capitalize(name)
            `object` = ReflectUtil.invoke(`object`, getterMethodName)
        }
        return `object` as E
    }

    /**
     * 调用Setter方法, 仅匹配方法名。
     * 支持多级，如：对象名.对象名.方法
     */
    @JvmStatic
    fun <E> invokeSetter(obj: Any, propertyName: String, value: E) {
        var `object` = obj
        val names: Array<String> = StringUtils.split(propertyName, ".")
        for (i in names.indices) {
            if (i < names.size - 1) {
                val getterMethodName = GETTER_PREFIX + StringUtils.capitalize(names[i])
                `object` = ReflectUtil.invoke(`object`, getterMethodName)
            } else {
                val setterMethodName = SETTER_PREFIX + StringUtils.capitalize(names[i])
                val method = ReflectUtil.getMethodByName(`object`.javaClass, setterMethodName)
                ReflectUtil.invoke<Any>(`object`, method, value)
            }
        }
    }
}
