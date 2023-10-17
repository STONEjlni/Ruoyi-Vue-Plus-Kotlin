package com.blank.generator.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.PropertySource
import org.springframework.stereotype.Component

/**
 * 读取代码生成相关配置
 */
@Component
@ConfigurationProperties(prefix = "gen")
@PropertySource(value = ["classpath:generator.yml"], encoding = "UTF-8")
class GenConfig {
    @Value("\${author}")
    fun setAuthor(author: String?) {
        Companion.author = author
    }

    @Value("\${packageName}")
    fun setPackageName(packageName: String?) {
        Companion.packageName = packageName
    }

    @Value("\${autoRemovePre}")
    fun setAutoRemovePre(autoRemovePre: Boolean) {
        Companion.autoRemovePre = autoRemovePre
    }

    @Value("\${tablePrefix}")
    fun setTablePrefix(tablePrefix: String?) {
        Companion.tablePrefix = tablePrefix
    }

    companion object {
        /**
         * 作者
         */
        var author: String? = null

        /**
         * 生成包路径
         */
        var packageName: String? = null

        /**
         * 自动去除表前缀，默认是false
         */
        var autoRemovePre = false

        /**
         * 表前缀(类名不会包含表前缀)
         */
        var tablePrefix: String? = null
    }
}

