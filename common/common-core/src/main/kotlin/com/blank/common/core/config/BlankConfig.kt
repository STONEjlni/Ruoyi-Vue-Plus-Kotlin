package com.blank.common.core.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 读取项目相关配置
 */
@Component
@ConfigurationProperties(prefix = "blank")
class BlankConfig {
    /**
     * 项目名称
     */
    var name: String = ""

    /**
     * 版本
     */
    var version: String = ""

    /**
     * 版权年份
     */
    var copyrightYear: String = ""
}
