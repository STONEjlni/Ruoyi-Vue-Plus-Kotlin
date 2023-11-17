package com.blank.common.social.maxkey

import cn.hutool.extra.spring.SpringUtil
import com.blank.common.json.utils.JsonUtils.parseMap
import me.zhyd.oauth.cache.AuthDefaultStateCache
import me.zhyd.oauth.cache.AuthStateCache
import me.zhyd.oauth.config.AuthConfig
import me.zhyd.oauth.exception.AuthException
import me.zhyd.oauth.model.AuthCallback
import me.zhyd.oauth.model.AuthToken
import me.zhyd.oauth.model.AuthUser
import me.zhyd.oauth.request.AuthDefaultRequest

/**
 * Oauth2 默认接口说明
 */
class AuthMaxKeyRequest
/**
 * 设定归属域
 */
@JvmOverloads
constructor(config: AuthConfig, authStateCache: AuthStateCache? = AuthDefaultStateCache.INSTANCE) :
    AuthDefaultRequest(config, AuthMaxKeySource.MAXKEY, authStateCache) {
    companion object {
        val SERVER_URL: String = SpringUtil.getProperty("justauth.type.maxkey.server-url")
    }

    override fun getAccessToken(authCallback: AuthCallback): AuthToken {
        val body = doPostAuthorizationCode(authCallback.code)
        val `object` = parseMap(body)
        // oauth/token 验证异常
        if (`object`!!.containsKey("error")) {
            throw AuthException(`object`.getStr("error_description"))
        }
        // user 验证异常
        if (`object`.containsKey("message")) {
            throw AuthException(`object`.getStr("message"))
        }
        return AuthToken.builder()
            .accessToken(`object`.getStr("access_token"))
            .refreshToken(`object`.getStr("refresh_token"))
            .idToken(`object`.getStr("id_token"))
            .tokenType(`object`.getStr("token_type"))
            .scope(`object`.getStr("scope"))
            .build()
    }

    override fun getUserInfo(authToken: AuthToken?): AuthUser {
        val body = doGetUserInfo(authToken)
        val `object` = parseMap(body)
        // oauth/token 验证异常
        if (`object`!!.containsKey("error")) {
            throw AuthException(`object`.getStr("error_description"))
        }
        // user 验证异常
        if (`object`.containsKey("message")) {
            throw AuthException(`object`.getStr("message"))
        }
        return AuthUser.builder()
            .uuid(`object`.getStr("userId"))
            .username(`object`.getStr("username"))
            .nickname(`object`.getStr("displayName"))
            .avatar(`object`.getStr("avatar_url"))
            .blog(`object`.getStr("web_url"))
            .company(`object`.getStr("organization"))
            .location(`object`.getStr("location"))
            .email(`object`.getStr("email"))
            .remark(`object`.getStr("bio"))
            .token(authToken)
            .source(source.toString())
            .build()
    }
}
