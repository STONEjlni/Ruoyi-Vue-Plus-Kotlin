package com.blank.system.controller.monitor

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.system.domain.vo.CacheListInfoVo
import org.apache.commons.lang3.StringUtils
import org.redisson.spring.data.connection.RedissonConnectionFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.function.Consumer

/**
 * 缓存监控
 */
@RestController
@RequestMapping("/monitor/cache")
class CacheController(
    private val connectionFactory: RedissonConnectionFactory
) {
    @get:Throws(Exception::class)
    @get:GetMapping
    @get:SaCheckPermission("monitor:cache:list")
    val info: R<CacheListInfoVo>
        /**
         * 获取缓存监控列表
         */
        get() {
            val connection = connectionFactory.connection
            val commandStats = connection.commands().info("commandstats")
            val pieList: MutableList<Map<String, String>> = ArrayList()
            commandStats?.stringPropertyNames()?.forEach(Consumer { key: String? ->
                val `data`: MutableMap<String, String> = HashMap(2)
                val property = commandStats.getProperty(key)
                `data`["name"] = StringUtils.removeStart(key, "cmdstat_")
                `data`["value"] = StringUtils.substringBetween(property, "calls=", ",usec")
                pieList.add(`data`)
            })
            val infoVo = CacheListInfoVo()
            infoVo.info = connection.commands().info()
            infoVo.dbSize = connection.commands().dbSize()
            infoVo.commandStats = pieList
            return ok(data = infoVo)
        }
}
