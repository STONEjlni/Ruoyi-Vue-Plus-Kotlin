package com.blank.common.redis.manager

import com.blank.common.redis.utils.RedisUtils
import org.redisson.spring.cache.CacheConfig
import org.redisson.spring.cache.RedissonCache
import org.springframework.boot.convert.DurationStyle
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.transaction.TransactionAwareCacheDecorator
import org.springframework.util.StringUtils
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/**
 * A {@link org.springframework.cache.CacheManager} implementation
 * backed by Redisson instance.
 * <p>
 * 修改 RedissonSpringCacheManager 源码
 * 重写 cacheName 处理方法 支持多参数
 *
 */
class PlusSpringCacheManager : CacheManager {
    var dynamic = true
    var allowNullValues = true
    var transactionAware = true
    var configMap: MutableMap<String, CacheConfig> = ConcurrentHashMap()
    var instanceMap: MutableMap<String, Cache> = ConcurrentHashMap()

    /**
     * Defines 'fixed' cache names.
     * A new cache instance will not be created in dynamic for non-defined names.
     *
     *
     * `null` parameter setups dynamic mode
     *
     * @param names of caches
     */
    fun setCacheNames(names: Collection<String>?) {
        dynamic = if (names != null) {
            for (name in names) {
                getCache(name)
            }
            false
        } else {
            true
        }
    }

    fun createDefaultConfig() = CacheConfig()

    override fun getCache(name: String): Cache? {
        // 重写 cacheName 支持多参数
        var name = name
        val array = StringUtils.delimitedListToStringArray(name, "#")
        name = array[0]
        val cache = instanceMap[name]
        if (cache != null) {
            return cache
        }
        if (!dynamic) {
            return cache
        }
        var config = configMap[name]
        if (config == null) {
            config = createDefaultConfig()
            configMap.put(name, config)
        }
        if (array.size > 1) {
            config.ttl = DurationStyle.detectAndParse(array[1]).toMillis()
        }
        if (array.size > 2) {
            config.maxIdleTime = DurationStyle.detectAndParse(array[2]).toMillis()
        }
        if (array.size > 3) {
            config.maxSize = array[3].toInt()
        }
        return if (config.maxIdleTime == 0L && config.ttl == 0L && config.maxSize == 0) {
            createMap(name, config)
        } else createMapCache(name, config)
    }

    private fun createMap(name: String, config: CacheConfig?): Cache {
        val map = RedisUtils.getClient().getMap<Any, Any>(name)
        var cache: Cache = RedissonCache(map, allowNullValues)
        if (transactionAware) {
            cache = TransactionAwareCacheDecorator(cache)
        }
        val oldCache = instanceMap.putIfAbsent(name, cache)
        if (oldCache != null) {
            cache = oldCache
        }
        return cache
    }

    private fun createMapCache(name: String, config: CacheConfig): Cache {
        val map = RedisUtils.getClient().getMapCache<Any, Any>(name)
        var cache: Cache = RedissonCache(map, config, allowNullValues)
        if (transactionAware) {
            cache = TransactionAwareCacheDecorator(cache)
        }
        val oldCache = instanceMap.putIfAbsent(name, cache)
        if (oldCache != null) {
            cache = oldCache
        } else {
            map.setMaxSize(config.maxSize)
        }
        return cache
    }

    override fun getCacheNames(): Collection<String> {
        return Collections.unmodifiableSet(configMap.keys)
    }


}
