package com.blank.system.domain.bo

import com.blank.common.core.xss.Xss
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.common.sensitive.annotation.Sensitive
import com.blank.common.sensitive.core.SensitiveStrategy
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

/**
 * 个人信息业务处理
 */
class SysUserProfileBo : BaseEntity() {
    /**
     * 用户ID
     */
    var userId: Long? = null

    /**
     * 用户昵称
     */
    @Xss(message = "用户昵称不能包含脚本字符")
    var nickName: @Size(min = 0, max = 30, message = "用户昵称长度不能超过{max}个字符") String? = null

    /**
     * 用户邮箱
     */
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    var email: @Email(message = "邮箱格式不正确") @Size(
        min = 0,
        max = 50,
        message = "邮箱长度不能超过{max}个字符"
    ) String? = null

    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    var phonenumber: String? = null

    /**
     * 用户性别（0男 1女 2未知）
     */
    var sex: String? = null
}
