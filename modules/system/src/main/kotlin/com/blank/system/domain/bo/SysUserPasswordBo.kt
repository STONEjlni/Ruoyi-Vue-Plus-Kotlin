package com.blank.system.domain.bo

import jakarta.validation.constraints.NotBlank
import java.io.Serial
import java.io.Serializable


/**
 * 用户密码修改bo
 */
class SysUserPasswordBo : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 旧密码
     */
    @NotBlank(message = "旧密码不能为空")
    var oldPassword: String? = null

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    var newPassword: String? = null

}
