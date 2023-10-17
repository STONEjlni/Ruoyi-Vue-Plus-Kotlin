package com.blank.web.domain.vo

import com.fasterxml.jackson.annotation.JsonProperty

/**
 * 登录验证信息
 */
class LoginVo {
    /**
     * 授权令牌
     */
    @JsonProperty("access_token")
    var accessToken: String? = null

    /**
     * 刷新令牌
     */
    @JsonProperty("refresh_token")
    var refreshToken: String? = null

    /**
     * 授权令牌 access_token 的有效期
     */
    @JsonProperty("expire_in")
    var expireIn: Long? = null

    /**
     * 刷新令牌 refresh_token 的有效期
     */
    @JsonProperty("refresh_expire_in")
    var refreshExpireIn: Long? = null

    /**
     * 应用id
     */
    @JsonProperty("client_id")
    var clientId: String? = null

    /**
     * 令牌权限
     */
    var scope: String? = null

    /**
     * 用户 openid
     */
    var openid: String? = null
}
