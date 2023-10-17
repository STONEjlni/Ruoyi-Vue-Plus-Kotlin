package com.blank.common.websocket.listener

import cn.hutool.core.collection.CollUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.websocket.dto.WebSocketMessageDto
import com.blank.common.websocket.holder.WebSocketSessionHolder.existSession
import com.blank.common.websocket.holder.WebSocketSessionHolder.getSessionsAll
import com.blank.common.websocket.utils.WebSocketUtils
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.Ordered
import java.util.function.Consumer

/**
 * WebSocket 主题订阅监听器
 */
@Slf4j
class WebSocketTopicListener : ApplicationRunner, Ordered {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments?) {
        WebSocketUtils.subscribeMessage { message: WebSocketMessageDto ->
            log.info {
                "WebSocket主题订阅收到消息session keys=${message.sessionKeys} message=${message.message}"
            }
            // 如果key不为空就按照key发消息 如果为空就群发
            if (CollUtil.isNotEmpty(message.sessionKeys)) {
                message.sessionKeys!!.forEach(Consumer { key: Long? ->
                    if (existSession(
                            key!!
                        )
                    ) {
                        WebSocketUtils.sendMessage(key, message.message)
                    }
                })
            } else {
                getSessionsAll()
                    .forEach(Consumer { key: Long? ->
                        WebSocketUtils.sendMessage(
                            key,
                            message.message
                        )
                    })
            }
        }
        log.info { "初始化WebSocket主题订阅监听器成功" }
    }

    override fun getOrder(): Int {
        return -1
    }
}
