package com.blank.common.core.constant

/**
 * 缓存组名称常量
 * <p>
 * key 格式为 cacheNames#ttl#maxIdleTime#maxSize
 * <p>
 * ttl 过期时间 如果设置为0则不过期 默认为0
 * maxIdleTime 最大空闲时间 根据LRU算法清理空闲数据 如果设置为0则不检测 默认为0
 * maxSize 组最大长度 根据LRU算法清理溢出数据 如果设置为0则无限长 默认为0
 * <p>
 * 例子: test#60s、test#0#60s、test#0#1m#1000、test#1h#0#500
 */
interface CacheNames {
    companion object {
        /**
         * 演示案例
         */
        const val DEMO_CACHE = "demo:cache#60s#10m#20"

        /**
         * 系统配置
         */
        const val SYS_CONFIG = "sys_config"

        /**
         * 数据字典
         */
        const val SYS_DICT = "sys_dict"

        /**
         * 租户
         */
        const val SYS_TENANT = GlobalConstants.GLOBAL_REDIS_KEY + "sys_tenant#30d"

        /**
         * 用户账户
         */
        const val SYS_USER_NAME = "sys_user_name#30d"

        /**
         * 部门
         */
        const val SYS_DEPT = "sys_dept#30d"

        /**
         * OSS内容
         */
        const val SYS_OSS = "sys_oss#30d"

        /**
         * OSS配置
         */
        const val SYS_OSS_CONFIG = "sys_oss_config"

        /**
         * 在线用户
         */
        const val ONLINE_TOKEN = "online_tokens"
    }
}
