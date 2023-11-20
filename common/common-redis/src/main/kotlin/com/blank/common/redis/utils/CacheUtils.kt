package com.blank.common.redis.utils

import cn.hutool.extra.spring.SpringUtil
import org.redisson.api.RMap
import org.springframework.cache.CacheManager

/**
 * 缓存操作工具类 {@link }
 */
object CacheUtils {
    private val CACHE_MANAGER = SpringUtil.getBean(CacheManager::class.java)

    /**
     * 获取缓存组内所有的KEY
     *
     * @param cacheNames 缓存组名称
     */
    fun keys(cacheNames: String?): MutableSet<Any> {
        val rmap = CACHE_MANAGER.getCache(cacheNames!!)!!
            .nativeCache as RMap<*, *>
        return rmap.keys.toMutableSet()
    }

    /**
     * 获取缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存key
     */
    fun <T> get(cacheNames: String, key: Any): T? {
        val wrapper = CACHE_MANAGER.getCache(
            cacheNames
        )!![key]
        return if (wrapper != null) wrapper.get() as T? else null
    }

    /**
     * 保存缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存key
     * @param value      缓存值
     */
    fun put(cacheNames: String?, key: Any?, value: Any?) {
        CACHE_MANAGER.getCache(cacheNames!!)!!.put(key!!, value)
    }

    /**
     * 删除缓存值
     *
     * @param cacheNames 缓存组名称
     * @param key        缓存key
     */
    fun evict(cacheNames: String?, key: Any?) {
        CACHE_MANAGER.getCache(cacheNames!!)!!.evict(key!!)
    }

    /**
     * 清空缓存值
     *
     * @param cacheNames 缓存组名称
     */
    fun clear(cacheNames: String?) {
        CACHE_MANAGER.getCache(cacheNames!!)!!.clear()
    }
}
