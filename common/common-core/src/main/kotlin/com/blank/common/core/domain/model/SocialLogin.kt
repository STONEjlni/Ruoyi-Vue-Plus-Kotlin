package com.blank.common.core.domain.model

/**
 * 第三方登录用户身份权限
 */
class SocialLogin : LoginUser() {
    /**
     * openid
     */
    var openid: String? = null
}
