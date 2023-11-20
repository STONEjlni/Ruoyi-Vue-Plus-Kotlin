package com.blank.system.domain.vo

import java.util.*

/**
 * 缓存监控列表信息
 */
class CacheListInfoVo {
    var info: Properties? = null
    var dbSize: Long? = null
    var commandStats: MutableList<MutableMap<String, String>>? = null
}
