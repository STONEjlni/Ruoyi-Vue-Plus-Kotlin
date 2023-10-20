package com.blank.common.oss.factory

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.constant.CacheNames
import com.blank.common.json.utils.JsonUtils.parseObject
import com.blank.common.oss.constant.OssConstant
import com.blank.common.oss.core.OssClient
import com.blank.common.oss.exception.OssException
import com.blank.common.oss.properties.OssProperties
import com.blank.common.redis.utils.CacheUtils
import com.blank.common.redis.utils.RedisUtils
import org.apache.commons.lang3.StringUtils
import java.util.concurrent.ConcurrentHashMap

/**
 * 文件上传Factory
 *
 */
@Slf4j
object OssFactory {
    private val CLIENT_CACHE: MutableMap<String, OssClient> = ConcurrentHashMap()


    /**
     * 获取默认实例
     */
    fun instance(): OssClient {
        // 获取redis 默认类型
        val configKey = RedisUtils.getCacheObject<String>(OssConstant.DEFAULT_CONFIG_KEY)!!
        if (StringUtils.isEmpty(configKey)) {
            throw OssException("文件存储服务类型无法找到!")
        }
        return instance(configKey)
    }

    /**
     * 根据类型获取实例
     */
    fun instance(configKey: String): OssClient {
        val json = CacheUtils.get<String>(CacheNames.SYS_OSS_CONFIG, configKey)
            ?: throw OssException("系统异常, '$configKey'配置信息不存在!")
        val properties = parseObject(
            json,
            OssProperties::class.java
        )!!
        // 使用租户标识避免多个租户相同key实例覆盖
        val key = properties.tenantId + ":" + configKey
        val client = CLIENT_CACHE[key]
        if (client == null) {
            CLIENT_CACHE[key] = OssClient(configKey, properties)
            log.info { "创建OSS实例 key => $configKey" }
            return CLIENT_CACHE[key]!!
        }
        // 配置不相同则重新构建
        if (!client.checkPropertiesSame(properties)) {
            CLIENT_CACHE[key] = OssClient(configKey, properties)
            log.info { "重载OSS实例 key => $configKey" }
            return CLIENT_CACHE[key]!!
        }
        return client
    }
}
