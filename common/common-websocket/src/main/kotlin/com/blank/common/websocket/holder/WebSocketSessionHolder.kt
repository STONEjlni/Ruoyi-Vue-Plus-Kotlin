package com.blank.common.websocket.holder

import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

/**
 * WebSocketSession 用于保存当前所有在线的会话信息
 */
object WebSocketSessionHolder {
    private val USER_SESSION_MAP: MutableMap<Long, WebSocketSession> = ConcurrentHashMap()

    fun addSession(sessionKey: Long, session: WebSocketSession) {
        USER_SESSION_MAP[sessionKey] = session
    }

    fun removeSession(sessionKey: Long) {
        if (USER_SESSION_MAP.containsKey(sessionKey)) {
            USER_SESSION_MAP.remove(sessionKey)
        }
    }

    fun getSessions(sessionKey: Long): WebSocketSession? {
        return USER_SESSION_MAP[sessionKey]
    }

    fun getSessionsAll(): Set<Long> {
        return USER_SESSION_MAP.keys
    }

    fun existSession(sessionKey: Long): Boolean {
        return USER_SESSION_MAP.containsKey(sessionKey)
    }
}
