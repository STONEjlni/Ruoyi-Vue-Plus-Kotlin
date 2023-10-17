package com.blank.common.websocket.constant

/**
 * websocket的常量配置
 */
interface WebSocketConstants {
    companion object {
        /**
         * websocketSession中的参数的key
         */
        const val LOGIN_USER_KEY = "loginUser"

        /**
         * 订阅的频道
         */
        const val WEB_SOCKET_TOPIC = "global:websocket"

        /**
         * 前端心跳检查的命令
         */
        const val PING = "ping"

        /**
         * 服务端心跳恢复的字符串
         */
        const val PONG = "pong"
    }
}
