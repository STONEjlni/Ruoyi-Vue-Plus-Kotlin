package com.blank.common.websocket.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * WebSocket 配置项
 */
@ConfigurationProperties("websocket")
class WebSocketProperties {
    var enabled: Boolean? = null

    /**
     * 路径
     */
    var path: String? = null

    /**
     * 设置访问源地址
     */
    var allowedOrigins: String? = null
}
