package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import java.io.Serial

/**
 * 社会化关系对象 sys_social
 */
@Table("sys_social")
@Open
@NoArg
class SysSocial : BaseEntity() {
    /**
     * 主键
     */
    @Id
    var id: Long? = null

    /**
     * 用户ID
     */
    var userId: Long? = null

    /**
     * 的唯一ID
     */
    var authId: String? = null

    /**
     * 用户来源
     */
    var source: String? = null

    /**
     * 用户的授权令牌
     */
    var accessToken: String? = null

    /**
     * 用户的授权令牌的有效期，部分平台可能没有
     */
    var expireIn = 0

    /**
     * 刷新令牌，部分平台可能没有
     */
    var refreshToken: String? = null

    /**
     * 用户的 open id
     */
    var openId: String? = null

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

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
