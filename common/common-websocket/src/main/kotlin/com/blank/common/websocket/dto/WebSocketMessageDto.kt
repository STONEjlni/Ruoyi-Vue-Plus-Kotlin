package com.blank.common.websocket.dto

import java.io.Serial
import java.io.Serializable

/**
 * 消息的dto
 */
class WebSocketMessageDto : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 需要推送到的session key 列表
     */
    var sessionKeys: MutableList<Long>? = null

    /**
     * 需要发送的消息
     */
    var message: String? = null
}
