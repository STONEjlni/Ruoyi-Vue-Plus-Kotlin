package com.blank.common.core.utils

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.map.MapUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.extra.spring.SpringUtil
import io.github.linpeilie.Converter

/**
 * Mapstruct 工具类
 * <p>参考文档：<a href="https://mapstruct.plus/introduction/quick-start.html">mapstruct-plus</a></p>
 */
object MapstructUtils {
    private val CONVERTER = SpringUtil.getBean(
        Converter::class.java
    )

    /**
     * 将 T 类型对象，转换为 desc 类型的对象并返回
     *
     * @param source 数据来源实体
     * @param desc   描述对象 转换后的对象
     * @return desc
     */
    @JvmStatic
    fun <T, V> convert(source: T, desc: Class<V>?): V? {
        if (ObjectUtil.isNull(source)) {
            return null
        }
        return if (ObjectUtil.isNull(desc)) {
            null
        } else CONVERTER.convert(source, desc)
    }

    /**
     * 将 T 类型对象，按照配置的映射字段规则，给 desc 类型的对象赋值并返回 desc 对象
     *
     * @param source 数据来源实体
     * @param desc   转换后的对象
     * @return desc
     */
    @JvmStatic
    fun <T, V> convert(source: T, desc: V): V? {
        if (ObjectUtil.isNull(source)) {
            return null
        }
        return if (ObjectUtil.isNull(desc)) {
            null
        } else CONVERTER.convert(source, desc)
    }

    /**
     * 将 T 类型的集合，转换为 desc 类型的集合并返回
     *
     * @param sourceList 数据来源实体列表
     * @param desc       描述对象 转换后的对象
     * @return desc
     */
    @JvmStatic
    fun <T, V> convert(sourceList: List<T?>?, desc: Class<V>?): MutableList<V>? {
        if (ObjectUtil.isNull(sourceList)) {
            return null
        }
        return if (CollUtil.isEmpty(sourceList)) {
            CollUtil.newArrayList()
        } else CONVERTER.convert(sourceList, desc)
    }

    /**
     * 将 Map 转换为 beanClass 类型的集合并返回
     *
     * @param map       数据来源
     * @param beanClass bean类
     * @return bean对象
     */
    @JvmStatic
    fun <T> convert(map: Map<String?, Any?>?, beanClass: Class<T>?): T? {
        if (MapUtil.isEmpty(map)) {
            return null
        }
        return if (ObjectUtil.isNull(beanClass)) {
            null
        } else CONVERTER.convert(map, beanClass)
    }
}
