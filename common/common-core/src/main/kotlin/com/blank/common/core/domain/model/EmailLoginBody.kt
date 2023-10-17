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
    var email: @NotBlank(message = "{user.email.not.blank}") @Email(message = "{user.email.not.valid}") String? = null

    /**
     * 邮箱code
     */
    var emailCode: @NotBlank(message = "{email.code.not.blank}") String? = null
}
