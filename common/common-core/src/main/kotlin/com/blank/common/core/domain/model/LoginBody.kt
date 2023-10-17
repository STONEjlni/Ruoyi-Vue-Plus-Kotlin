package com.blank.common.core.domain.model

import com.blank.common.core.annotation.Open
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.validate.auth.EmailGroup
import com.blank.common.core.validate.auth.PasswordGroup
import com.blank.common.core.validate.auth.SocialGroup
import com.blank.common.core.validate.auth.WechatGroup
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import org.hibernate.validator.constraints.Length

/**
 * 用户登录对象
 */
@Open
class LoginBody {
    /**
     * 客户端id
     */
    var clientId: @NotBlank(message = "{auth.clientid.not.blank}") String? = null

    /**
     * 客户端key
     */
    var clientKey: String? = null

    /**
     * 客户端秘钥
     */
    var clientSecret: String? = null

    /**
     * 授权类型
     */
    var grantType: @NotBlank(message = "{auth.grant.type.not.blank}") String? = null

    /**
     * 用户名
     */
    var username: @NotBlank(
        message = "{user.username.not.blank}",
        groups = [PasswordGroup::class]
    ) @Length(
        min = UserConstants.USERNAME_MIN_LENGTH,
        max = UserConstants.USERNAME_MAX_LENGTH,
        message = "{user.username.length.valid}",
        groups = [PasswordGroup::class]
    ) String? = null

    /**
     * 用户密码
     */
    var password: @NotBlank(
        message = "{user.password.not.blank}",
        groups = [PasswordGroup::class]
    ) @Length(
        min = UserConstants.PASSWORD_MIN_LENGTH,
        max = UserConstants.PASSWORD_MAX_LENGTH,
        message = "{user.password.length.valid}",
        groups = [PasswordGroup::class]
    ) String? = null

    /**
     * 验证码
     */
    var code: String? = null

    /**
     * 唯一标识
     */
    var uuid: String? = null

    /**
     * 邮箱
     */
    var email: @NotBlank(
        message = "{user.email.not.blank}",
        groups = [EmailGroup::class]
    ) @Email(message = "{user.email.not.valid}") String? = null

    /**
     * 邮箱code
     */
    var emailCode: @NotBlank(message = "{email.code.not.blank}", groups = [EmailGroup::class]) String? = null

    /**
     * 小程序code
     */
    var xcxCode: @NotBlank(message = "{xcx.code.not.blank}", groups = [WechatGroup::class]) String? = null

    /**
     * 第三方登录平台
     */
    var source: @NotBlank(message = "{social.source.not.blank}", groups = [SocialGroup::class]) String? = null

    /**
     * 第三方登录code
     */
    var socialCode: @NotBlank(message = "{social.code.not.blank}", groups = [SocialGroup::class]) String? = null

    /**
     * 第三方登录socialState
     */
    var socialState: @NotBlank(message = "{social.state.not.blank}", groups = [SocialGroup::class]) String? = null
}
