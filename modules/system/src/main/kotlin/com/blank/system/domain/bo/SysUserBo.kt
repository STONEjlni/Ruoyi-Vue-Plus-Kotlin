package com.blank.system.domain.bo

import com.blank.common.core.constant.UserConstants
import com.blank.common.core.xss.Xss
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysUser
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * 用户信息业务对象 sys_user
 */
@AutoMapper(target = SysUser::class, reverseConvertGenerate = false)
class SysUserBo @JvmOverloads constructor(
    /**
     * 用户ID
     */
    var userId: Long? = null
) : BaseEntity() {
    /**
     * 部门ID
     */
    var deptId: Long? = null

    /**
     * 用户账号
     */
    @Xss(message = "用户账号不能包含脚本字符")
    var userName: @NotBlank(message = "用户账号不能为空") @Size(
        min = 0,
        max = 30,
        message = "用户账号长度不能超过{max}个字符"
    ) String? = null

    /**
     * 用户昵称
     */
    @Xss(message = "用户昵称不能包含脚本字符")
    var nickName: @NotBlank(message = "用户昵称不能为空") @Size(
        min = 0,
        max = 30,
        message = "用户昵称长度不能超过{max}个字符"
    ) String? = null

    /**
     * 用户类型（sys_user系统用户）
     */
    var userType: String? = null

    /**
     * 用户邮箱
     */
    var email: @Email(message = "邮箱格式不正确") @Size(
        min = 0,
        max = 50,
        message = "邮箱长度不能超过{max}个字符"
    ) String? = null

    /**
     * 手机号码
     */
    var phonenumber: String? = null

    /**
     * 用户性别（0男 1女 2未知）
     */
    var sex: String? = null

    /**
     * 密码
     */
    var password: String? = null

    /**
     * 帐号状态（0正常 1停用）
     */
    var status: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 角色组
     */
    var roleIds: Array<Long>? = arrayOf()

    /**
     * 岗位组
     */
    var postIds: Array<Long>? = arrayOf()

    /**
     * 数据权限 当前角色ID
     */
    var roleId: Long? = null
    val isSuperAdmin: Boolean
        get() = UserConstants.SUPER_ADMIN_ID == userId
}
