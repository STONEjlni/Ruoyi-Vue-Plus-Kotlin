package com.blank.common.core.domain.model

import jakarta.validation.constraints.NotBlank

/**
 * 三方登录对象
 *
 */
class XcxLoginBody : LoginBody() {
    /**
     * 小程序id(多个小程序时使用)
     */
    var appid: String? = null

    /**
     * 小程序code
     */
    @NotBlank(message = "{xcx.code.not.blank}")
    var xcxCode: String? = null
}
