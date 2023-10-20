package com.blank.system.controller.system

import cn.dev33.satoken.secure.BCrypt
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.io.FileUtil
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.utils.file.MimeTypeUtils
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.satoken.utils.LoginHelper.getUserId
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.domain.bo.SysUserPasswordBo
import com.blank.system.domain.bo.SysUserProfileBo
import com.blank.system.domain.vo.AvatarVo
import com.blank.system.domain.vo.ProfileVo
import com.blank.system.service.ISysOssService
import com.blank.system.service.ISysUserService
import org.apache.commons.lang3.StringUtils
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

/**
 * 个人信息 业务处理
 */
@Validated
@RestController
@RequestMapping("/system/user/profile")
class SysProfileController(
    private val userService: ISysUserService,
    private val ossService: ISysOssService
) : BaseController() {

    /**
     * 个人信息
     */
    @GetMapping
    fun profile(): R<ProfileVo> {
        val user = userService.selectUserById(getUserId()!!)!!
        val profileVo = ProfileVo()
        profileVo.user = user
        profileVo.roleGroup = userService.selectUserRoleGroup(user.userName!!)
        return ok(data = profileVo)
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping
    fun updateProfile(@RequestBody profile: SysUserProfileBo?): R<Unit> {
        val user = BeanUtil.toBean(profile, SysUserBo::class.java)
        if (StringUtils.isNotEmpty(user.phonenumber) && !userService.checkPhoneUnique(user)) {
            return fail(msg = "修改用户'${user.userName}'失败，手机号码已存在")
        }
        if (StringUtils.isNotEmpty(user.email) && !userService.checkEmailUnique(user)) {
            return fail(msg = "修改用户'${user.userName}'失败，邮箱账号已存在")
        }
        user.userId = getUserId()!!
        return if (userService.updateUserProfile(user) > 0) {
            ok()
        } else fail(msg = "修改个人信息异常，请联系管理员")
    }

    /**
     * 重置密码
     *
     * @param bo 新旧密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    fun updatePwd(
        @Validated
        @RequestBody bo: SysUserPasswordBo
    ): R<Unit> {
        val user = userService.selectUserById(getUserId()!!)!!
        val password = user.password
        if (!BCrypt.checkpw(bo.oldPassword, password)) {
            return fail(msg = "修改密码失败，旧密码错误")
        }
        if (BCrypt.checkpw(bo.newPassword, password)) {
            return fail(msg = "新密码不能与旧密码相同")
        }
        return if (userService.resetUserPwd(user.userId!!, BCrypt.hashpw(bo.newPassword))) {
            ok()
        } else fail(msg = "修改密码异常，请联系管理员")
    }

    /**
     * 头像上传
     *
     * @param avatarfile 用户头像
     */
    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
    @PostMapping(value = ["/avatar"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun avatar(@RequestPart("avatarfile") avatarfile: MultipartFile): R<AvatarVo> {
        if (!avatarfile.isEmpty) {
            val extension = FileUtil.extName(avatarfile.originalFilename)
            if (!StringUtils.equalsAnyIgnoreCase(extension, *MimeTypeUtils.IMAGE_EXTENSION)) {
                return fail(msg = "文件格式不正确，请上传${MimeTypeUtils.IMAGE_EXTENSION.contentToString()}格式")
            }
            val oss = ossService.upload(avatarfile)!!
            val avatar = oss.url
            if (userService.updateUserAvatar(getUserId()!!, oss.ossId!!)) {
                val avatarVo = AvatarVo()
                avatarVo.imgUrl = avatar
                return ok(data = avatarVo)
            }
        }
        return fail(msg = "上传图片异常，请联系管理员")
    }
}
