package com.blank.common.sensitive.core

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 *
 */
interface SensitiveService {
    /**
     * 是否脱敏
     */
    fun isSensitive(): Boolean
}
