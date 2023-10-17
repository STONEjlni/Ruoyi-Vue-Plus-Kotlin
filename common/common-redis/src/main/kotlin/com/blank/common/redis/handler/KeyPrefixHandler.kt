package com.blank.common.redis.handler

import cn.hutool.core.util.StrUtil
import org.redisson.api.NameMapper

/**
 * redis缓存key前缀处理
 */
class KeyPrefixHandler(
    private var keyPrefix: String
) : NameMapper {
    init {
        //前缀为空 则返回空前缀
        this.keyPrefix = if (StrUtil.isBlank(keyPrefix)) "" else "$keyPrefix:"
    }

    /**
     * 增加前缀
     */
    override fun map(name: String): String? {
        if (StrUtil.isBlank(name)) {
            return null
        }
        return if (StrUtil.isNotBlank(keyPrefix) && !name.startsWith(keyPrefix)) {
            keyPrefix + name
        } else name
    }

    /**
     * 去除前缀
     */
    override fun unmap(name: String): String? {
        if (StrUtil.isBlank(name)) {
            return null
        }
        return if (StrUtil.isNotBlank(keyPrefix) && name.startsWith(keyPrefix)) {
            name.substring(keyPrefix.length)
        } else name
    }
}
