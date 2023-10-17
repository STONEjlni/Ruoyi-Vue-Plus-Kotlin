package com.blank.system.domain.bo

import com.blank.common.core.validate.AddGroup
import com.blank.common.core.validate.EditGroup
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysSocial
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

/**
 * 社会化关系业务对象 sys_social
 */
@AutoMapper(target = SysSocial::class, reverseConvertGenerate = false)
class SysSocialBo : BaseEntity() {
    /**
     * 主键
     */
    var id: @NotNull(message = "主键不能为空", groups = [EditGroup::class]) Long? = null

    /**
     * 的唯一ID
     */
    var authId: @NotBlank(message = "的唯一ID不能为空", groups = [AddGroup::class, EditGroup::class]) String? =
        null

    /**
     * 用户来源
     */
    var source: @NotBlank(message = "用户来源不能为空", groups = [AddGroup::class, EditGroup::class]) String? =
        null

    /**
     * 用户的授权令牌
     */
    var accessToken: @NotBlank(
        message = "用户的授权令牌不能为空",
        groups = [AddGroup::class, EditGroup::class]
    ) String? = null

    /**
     * 用户的授权令牌的有效期，部分平台可能没有
     */
    var expireIn = 0

    /**
     * 刷新令牌，部分平台可能没有
     */
    var refreshToken: String? = null

    /**
     * 平台唯一id
     */
    var openId: String? = null

    /**
     * 用户的 ID
     */
    var userId: @NotBlank(message = "用户的 ID不能为空", groups = [AddGroup::class, EditGroup::class]) Long? =
        null

    /**
     * 平台的授权信息，部分平台可能没有
     */
    var accessCode: String? = null

    /**
     * 用户的 unionid
     */
    var unionId: String? = null

    /**
     * 授予的权限，部分平台可能没有
     */
    var scope: String? = null

    /**
     * 授权的第三方账号
     */
    var userName: String? = null

    /**
     * 授权的第三方昵称
     */
    var nickName: String? = null

    /**
     * 授权的第三方邮箱
     */
    var email: String? = null

    /**
     * 授权的第三方头像地址
     */
    var avatar: String? = null

    /**
     * 个别平台的授权信息，部分平台可能没有
     */
    var tokenType: String? = null

    /**
     * id token，部分平台可能没有
     */
    var idToken: String? = null

    /**
     * 小米平台用户的附带属性，部分平台可能没有
     */
    var macAlgorithm: String? = null

    /**
     * 小米平台用户的附带属性，部分平台可能没有
     */
    var macKey: String? = null

    /**
     * 用户的授权code，部分平台可能没有
     */
    var code: String? = null

    /**
     * Twitter平台用户的附带属性，部分平台可能没有
     */
    var oauthToken: String? = null

    /**
     * Twitter平台用户的附带属性，部分平台可能没有
     */
    var oauthTokenSecret: String? = null
}
