package com.blank.common.social.utils

import cn.hutool.core.util.ObjectUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.domain.model.LoginBody
import com.blank.common.social.config.properties.SocialProperties
import com.blank.common.social.maxkey.AuthMaxKeyRequest
import me.zhyd.oauth.config.AuthConfig
import me.zhyd.oauth.exception.AuthException
import me.zhyd.oauth.model.AuthCallback
import me.zhyd.oauth.model.AuthResponse
import me.zhyd.oauth.model.AuthUser
import me.zhyd.oauth.request.*
import java.util.*

/**
 * 认证授权工具类
 */
object SocialUtils {
    private val STATE_CACHE = SpringUtil.getBean(
        AuthRedisStateCache::class.java
    )

    @Throws(AuthException::class)
    @JvmStatic
    fun loginAuth(loginBody: LoginBody, socialProperties: SocialProperties): AuthResponse<AuthUser> {
        val authRequest = getAuthRequest(loginBody.source, socialProperties)
        val callback = AuthCallback()
        callback.code = loginBody.socialCode
        callback.state = loginBody.socialState
        return authRequest.login(callback) as AuthResponse<AuthUser>
    }

    @Throws(AuthException::class)
    @JvmStatic
    fun getAuthRequest(source: String?, socialProperties: SocialProperties): AuthRequest {
        val obj = socialProperties.type!![source!!]
        if (ObjectUtil.isNull(obj)) {
            throw AuthException("不支持的第三方登录类型")
        }
        val clientId = obj!!.clientId
        val clientSecret = obj.clientSecret
        val redirectUri = obj.redirectUri
        return when (source.lowercase(Locale.getDefault())) {
            "dingtalk" -> AuthDingTalkRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "baidu" -> AuthBaiduRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "github" -> AuthGithubRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "gitee" -> AuthGiteeRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "weibo" -> AuthWeiboRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "coding" -> AuthCodingRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "oschina" -> AuthOschinaRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "alipay_wallet" -> AuthAlipayRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                socialProperties.type!!["alipay_wallet"]!!.alipayPublicKey,
                STATE_CACHE
            )

            "qq" -> AuthQqRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "wechat_open" -> AuthWeChatOpenRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "taobao" -> AuthTaobaoRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "douyin" -> AuthDouyinRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "linkedin" -> AuthLinkedinRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "microsoft" -> AuthMicrosoftRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "renren" -> AuthRenrenRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "stack_overflow" -> AuthStackOverflowRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri)
                    .stackOverflowKey("").build(), STATE_CACHE
            )

            "huawei" -> AuthHuaweiRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "wechat_enterprise" -> AuthWeChatEnterpriseQrcodeRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).agentId("")
                    .build(), STATE_CACHE
            )

            "gitlab" -> AuthGitlabRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "wechat_mp" -> AuthWeChatMpRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "aliyun" -> AuthAliyunRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            "maxkey" -> AuthMaxKeyRequest(
                AuthConfig.builder().clientId(clientId).clientSecret(clientSecret).redirectUri(redirectUri).build(),
                STATE_CACHE
            )

            else -> throw AuthException("未获取到有效的Auth配置")
        }
    }
}
