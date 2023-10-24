package com.blank.system.domain.dto

import com.blank.common.sensitive.annotation.Sensitive
import com.blank.common.sensitive.core.SensitiveStrategy
import com.blank.common.translation.annotation.Translation
import com.blank.common.translation.constant.TransConstant
import com.blank.system.domain.vo.SysDeptVo
import com.blank.system.domain.vo.SysRoleVo
import com.blank.system.domain.vo.SysUserVo
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

@AutoMapper(target = SysUserVo::class)
class SysUserDto : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 用户ID
     */
    var userId: Long? = null


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
    @Sensitive(strategy = SensitiveStrategy.EMAIL)
    var email: String? = null

    /**
     * 手机号码
     */
    @Sensitive(strategy = SensitiveStrategy.PHONE)
    var phonenumber: String? = null

    /**
     * 用户性别（0男 1女 2未知）
     */
    var sex: String? = null

    /**
     * 头像地址
     */
    @Translation(type = TransConstant.OSS_ID_TO_URL)
    var avatar: Long? = null

    /**
     * 密码
     */
    @JsonIgnore
    @JsonProperty
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
     * 创建时间
     */
    var createTime: Date? = null

    /**
     * 部门对象
     */
    var dept: SysDeptVo? = null

    /**
     * 角色对象
     */
    var roles: MutableList<SysRoleVo>? = null
}
