package com.blank.common.social.maxkey

import me.zhyd.oauth.config.AuthSource
import me.zhyd.oauth.request.AuthDefaultRequest

/**
 * Oauth2 默认接口说明
 */
enum class AuthMaxKeySource : AuthSource {
    /**
     * 自己搭建的 maxkey 私服
     */
    MAXKEY {
        /**
         * 授权的api
         */
        override fun authorize(): String {
            return AuthMaxKeyRequest.SERVER_URL + "/sign/authz/oauth/v20/authorize"
        }

        /**
         * 获取accessToken的api
         */
        override fun accessToken(): String {
            return AuthMaxKeyRequest.SERVER_URL + "/sign/authz/oauth/v20/token"
        }

        /**
         * 获取用户信息的api
         */
        override fun userInfo(): String {
            return AuthMaxKeyRequest.SERVER_URL + "/sign/api/oauth/v20/me"
        }

        /**
         * 平台对应的 AuthRequest 实现类，必须继承自 [AuthDefaultRequest]
         */
        override fun getTargetClass(): Class<out AuthDefaultRequest> {
            return AuthMaxKeyRequest::class.java
        }
    }
}
