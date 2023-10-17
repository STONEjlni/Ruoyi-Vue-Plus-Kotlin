package com.blank.common.websocket.config

import cn.hutool.core.util.StrUtil
import com.blank.common.websocket.config.properties.WebSocketProperties
import com.blank.common.websocket.handler.PlusWebSocketHandler
import com.blank.common.websocket.interceptor.PlusWebSocketInterceptor
import com.blank.common.websocket.listener.WebSocketTopicListener
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry
import org.springframework.web.socket.server.HandshakeInterceptor

/**
 * WebSocket 配置
 */
@AutoConfiguration
@ConditionalOnProperty(value = ["websocket.enabled"], havingValue = "true")
@EnableConfigurationProperties(WebSocketProperties::class)
@EnableWebSocket
class WebSocketConfig {
    @Bean
    fun webSocketConfigurer(
        handshakeInterceptor: HandshakeInterceptor?,
        webSocketHandler: WebSocketHandler?,
        webSocketProperties: WebSocketProperties
    ): WebSocketConfigurer {
        if (StrUtil.isBlank(webSocketProperties.path)) {
            webSocketProperties.path = "/websocket"
        }
        if (StrUtil.isBlank(webSocketProperties.allowedOrigins)) {
            webSocketProperties.allowedOrigins = "*"
        }
        return WebSocketConfigurer { registry: WebSocketHandlerRegistry ->
            registry
                .addHandler(webSocketHandler!!, webSocketProperties.path)
                .addInterceptors(handshakeInterceptor)
                .setAllowedOrigins(webSocketProperties.allowedOrigins)
        }
    }

    @Bean
    fun handshakeInterceptor(): HandshakeInterceptor {
        return PlusWebSocketInterceptor()
    }

    @Bean
    fun webSocketHandler(): WebSocketHandler {
        return PlusWebSocketHandler()
    }

    @Bean
    fun topicListener(): WebSocketTopicListener {
        return WebSocketTopicListener()
    }
}
