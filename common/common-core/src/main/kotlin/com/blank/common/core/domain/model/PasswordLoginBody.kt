package com.blank.common.core.domain.model

import com.blank.common.core.constant.UserConstants.Companion.PASSWORD_MAX_LENGTH
import com.blank.common.core.constant.UserConstants.Companion.PASSWORD_MIN_LENGTH
import com.blank.common.core.constant.UserConstants.Companion.USERNAME_MAX_LENGTH
import com.blank.common.core.constant.UserConstants.Companion.USERNAME_MIN_LENGTH
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

/**
 * 密码登录对象
 *
 */
class PasswordLoginBody : LoginBody() {
    /**
     * 用户名
     */
    @NotBlank(message = "{user.username.not.blank}")
    @Length(min = USERNAME_MIN_LENGTH, max = USERNAME_MAX_LENGTH, message = "{user.username.length.valid}")
    var username: String? = null

    /**
     * 用户密码
     */
    @NotBlank(message = "{user.password.not.blank}")
    @Length(min = PASSWORD_MIN_LENGTH, max = PASSWORD_MAX_LENGTH, message = "{user.password.length.valid}")
    var password: String? = null
}
