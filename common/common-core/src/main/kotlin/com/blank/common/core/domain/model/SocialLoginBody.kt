package com.blank.common.core.domain.model

import jakarta.validation.constraints.NotBlank

/**
 * 三方登录对象
 *
 */
class SocialLoginBody : LoginBody() {
    /**
     * 第三方登录平台
     */
    @NotBlank(message = "{social.source.not.blank}")
    var source: String? = null

    /**
     * 第三方登录code
     */
    @NotBlank(message = "{social.code.not.blank}")
    var socialCode: String? = null

    /**
     * 第三方登录socialState
     */
    @NotBlank(message = "{social.state.not.blank}")
    var socialState: String? = null
}
