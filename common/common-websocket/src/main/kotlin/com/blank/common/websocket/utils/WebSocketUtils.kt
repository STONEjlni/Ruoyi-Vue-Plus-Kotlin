package com.blank.common.websocket.utils

import cn.hutool.core.collection.CollUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.domain.model.LoginUser
import com.blank.common.redis.utils.RedisUtils.publish
import com.blank.common.redis.utils.RedisUtils.subscribe
import com.blank.common.websocket.constant.WebSocketConstants
import com.blank.common.websocket.dto.WebSocketMessageDto
import com.blank.common.websocket.holder.WebSocketSessionHolder.existSession
import com.blank.common.websocket.holder.WebSocketSessionHolder.getSessions
import com.blank.common.websocket.holder.WebSocketSessionHolder.getSessionsAll
import org.springframework.web.socket.PongMessage
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import java.io.IOException
import java.util.function.Consumer


/**
 * 工具类
 */
@Slf4j
object WebSocketUtils {
    /**
     * 发送消息
     *
     * @param sessionKey session主键 一般为用户id
     * @param message    消息文本
     */
    @JvmStatic
    fun sendMessage(sessionKey: Long?, message: String?) {
        val session = getSessions(
            sessionKey!!
        )
        sendMessage(session, message)
    }

    /**
     * 订阅消息
     *
     * @param consumer 自定义处理
     */
    @JvmStatic
    fun subscribeMessage(consumer: Consumer<WebSocketMessageDto>) {
        subscribe(
            WebSocketConstants.WEB_SOCKET_TOPIC,
            WebSocketMessageDto::class.java, consumer
        )
    }

    /**
     * 发布订阅的消息
     *
     * @param webSocketMessage 消息对象
     */
    @JvmStatic
    fun publishMessage(webSocketMessage: WebSocketMessageDto) {
        val unsentSessionKeys: MutableList<Long> = mutableListOf()
        // 当前服务内session,直接发送消息
        for (sessionKey in webSocketMessage.sessionKeys!!) {
            if (existSession(sessionKey)) {
                sendMessage(sessionKey, webSocketMessage.message)
                continue
            }
            unsentSessionKeys.add(sessionKey)
        }
        // 不在当前服务内session,发布订阅消息
        if (CollUtil.isNotEmpty(unsentSessionKeys)) {
            val broadcastMessage = WebSocketMessageDto()
            broadcastMessage.message = webSocketMessage.message
            broadcastMessage.sessionKeys = unsentSessionKeys
            publish<WebSocketMessageDto>(
                WebSocketConstants.WEB_SOCKET_TOPIC, broadcastMessage
            ) {
                log.info {
                    "WebSocket发送主题订阅消息topic:${WebSocketConstants.WEB_SOCKET_TOPIC} session " +
                        "keys:$unsentSessionKeys message:$webSocketMessage.message"
                }
            }
        }
    }

    /**
     * 发布订阅的消息(群发)
     *
     * @param message 消息内容
     */
    @JvmStatic
    fun publishAll(message: String?) {
        getSessionsAll().forEach(Consumer { key: Long? ->
            sendMessage(
                key,
                message
            )
        })
        val broadcastMessage = WebSocketMessageDto()
        broadcastMessage.message = message
        publish(
            WebSocketConstants.WEB_SOCKET_TOPIC, broadcastMessage
        ) {
            WebSocketUtils.log.info {
                " WebSocket发送主题订阅消息topic:${WebSocketConstants.WEB_SOCKET_TOPIC} message:$message"
            }
        }
    }

    @JvmStatic
    fun sendPongMessage(session: WebSocketSession?) {
        sendMessage(session, PongMessage())
    }

    @JvmStatic
    fun sendMessage(session: WebSocketSession?, message: String?) {
        sendMessage(session, TextMessage(message!!))
    }

    @JvmStatic
    private fun sendMessage(session: WebSocketSession?, message: WebSocketMessage<*>) {
        if (session == null || !session.isOpen) {
            log.error { "[send] session会话已经关闭" }
        } else {
            try {
                // 获取当前会话中的用户
                val loginUser = session.attributes[WebSocketConstants.LOGIN_USER_KEY] as LoginUser?
                session.sendMessage(message)
                log.info {
                    "[send] sessionId: ${session.id},userId:${loginUser!!.userId},userType:${loginUser.userType},message:$message"
                }
            } catch (e: IOException) {
                log.error(e) { "[send] session($session) 发送消息($message) 异常" }
            }
        }
    }
}
