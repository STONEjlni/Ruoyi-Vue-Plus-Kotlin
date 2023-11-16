package com.blank.common.core.domain.model

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank

/**
 * 邮件登录对象
 */
class EmailLoginBody {
    /**
     * 邮箱
     */
    @NotBlank(message = "{user.email.not.blank}")
    @Email(message = "{user.email.not.valid}")
    var email: @NotBlank(message = "{user.email.not.blank}") @Email(message = "{user.email.not.valid}") String? = null

    /**
     * 邮箱code
     */
    @NotBlank(message = "{email.code.not.blank}")
    var emailCode: @NotBlank(message = "{email.code.not.blank}") String? = null
}
