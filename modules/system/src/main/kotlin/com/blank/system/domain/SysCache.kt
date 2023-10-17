package com.blank.system.domain

import org.apache.commons.lang3.StringUtils

/**
 * 缓存信息
 */
class SysCache {
    /**
     * 缓存名称
     */
    var cacheName = ""

    /**
     * 缓存键名
     */
    var cacheKey = ""

    /**
     * 缓存内容
     */
    var cacheValue = ""

    /**
     * 备注
     */
    var remark = ""

    constructor(cacheName: String, remark: String) {
        this.cacheName = cacheName
        this.remark = remark
    }

    constructor(cacheName: String?, cacheKey: String?, cacheValue: String) {
        this.cacheName = StringUtils.replace(cacheName, ":", "")
        this.cacheKey = StringUtils.replace(cacheKey, cacheName, "")
        this.cacheValue = cacheValue
    }
}
