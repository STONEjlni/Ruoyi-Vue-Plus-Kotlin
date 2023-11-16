package com.blank.common.core.domain.model

import com.blank.common.core.annotation.Open
import jakarta.validation.constraints.NotBlank
import java.io.Serial
import java.io.Serializable

/**
 * 用户登录对象
 */
@Open
class LoginBody : Serializable {

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 客户端id
     */
    var clientId: @NotBlank(message = "{auth.clientid.not.blank}") String? = null

    /**
     * 授权类型
     */
    var grantType: @NotBlank(message = "{auth.grant.type.not.blank}") String? = null

    /**
     * 验证码
     */
    var code: String? = null

    /**
     * 唯一标识
     */
    var uuid: String? = null
}
