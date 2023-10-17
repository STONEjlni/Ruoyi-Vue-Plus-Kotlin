package com.blank.common.websocket.interceptor

import com.blank.common.core.annotation.Slf4j
import com.blank.common.satoken.utils.LoginHelper.getLoginUser
import com.blank.common.websocket.constant.WebSocketConstants
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.lang.Nullable
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.HandshakeInterceptor

/**
 * WebSocket握手请求的拦截器
 */
@Slf4j
class PlusWebSocketInterceptor : HandshakeInterceptor {
    /**
     * 握手前
     *
     * @param request    request
     * @param response   response
     * @param wsHandler  wsHandler
     * @param attributes attributes
     * @return 是否握手成功
     */
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {
        val loginUser = getLoginUser()
        attributes[WebSocketConstants.LOGIN_USER_KEY] = loginUser!!
        return true
    }

    /**
     * 握手后
     *
     * @param request   request
     * @param response  response
     * @param wsHandler wsHandler
     * @param exception 异常
     */
    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        @Nullable exception: Exception?
    ) {
    }
}
