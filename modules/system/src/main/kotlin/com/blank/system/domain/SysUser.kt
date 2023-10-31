package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.core.constant.UserConstants
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import java.util.*

/**
 * 用户对象 sys_user
 */
@Table("sys_user")
@Open
@NoArg
class SysUser @JvmOverloads constructor(
    /**
     * 用户ID
     */
    @Id
    var userId: Long? = null
) : BaseEntity() {

    /**
     * 部门ID
     */
    var deptId: Long? = null

    /**
     * 用户账号
     */
    var userName: String? = null

    /**
     * 用户昵称
     */
    var nickName: String? = null

    /**
     * 用户类型（sys_user系统用户）
     */
    var userType: String? = null

    /**
     * 用户邮箱
     */
    var email: String? = null

    /**
     * 手机号码
     */
    var phonenumber: String? = null

    /**
     * 用户性别
     */
    var sex: String? = null

    /**
     * 用户头像
     */
    var avatar: Long? = null

    /**
     * 密码
     */
    var password: String? = null

    /**
     * 帐号状态（0正常 1停用）
     */
    var status: String? = null

    /**
     * 最后登录IP
     */
    var loginIp: String? = null

    /**
     * 最后登录时间
     */
    var loginDate: Date? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Column(isLogicDelete = true)
    var delFlag: String? = null

    val isSuperAdmin: Boolean
        get() = UserConstants.SUPER_ADMIN_ID == userId
}
