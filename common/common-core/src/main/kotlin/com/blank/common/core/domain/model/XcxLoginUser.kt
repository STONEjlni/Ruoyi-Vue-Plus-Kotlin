package com.blank.common.core.domain.model

import java.io.Serial

/**
 * 小程序登录用户身份权限
 */
class XcxLoginUser : LoginUser() {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * openid
     */
    var openid: String? = null
}
