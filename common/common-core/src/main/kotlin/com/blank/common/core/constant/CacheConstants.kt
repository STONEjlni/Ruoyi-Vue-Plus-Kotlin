package com.blank.common.core.constant

/**
 * 缓存的key 常量
 */
interface CacheConstants {
    companion object {
        /**
         * 在线用户 redis key
         */
        const val ONLINE_TOKEN_KEY = "online_tokens:"

        /**
         * 参数管理 cache key
         */
        const val SYS_CONFIG_KEY = "sys_config:"

        /**
         * 字典管理 cache key
         */
        const val SYS_DICT_KEY = "sys_dict:"
    }
}
