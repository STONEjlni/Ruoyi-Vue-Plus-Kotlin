package com.blank.web.service

import cn.dev33.satoken.secure.BCrypt
import com.blank.common.core.annotation.UserType.Companion.getUserType
import com.blank.common.core.constant.Constants
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.model.RegisterBody
import com.blank.common.core.exception.user.CaptchaException
import com.blank.common.core.exception.user.CaptchaExpireException
import com.blank.common.core.exception.user.UserException
import com.blank.common.core.utils.JakartaServletUtilExtend.getRequest
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.core.utils.SpringUtilExtend.context
import com.blank.common.log.event.LogininforEvent
import com.blank.common.redis.utils.RedisUtils.deleteObject
import com.blank.common.redis.utils.RedisUtils.getCacheObject
import com.blank.common.web.config.properties.CaptchaProperties
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.service.ISysUserService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

/**
 * 注册校验方法
 */
@Service
class SysRegisterService(
    private val userService: ISysUserService,
    private val captchaProperties: CaptchaProperties
) {


    /**
     * 注册
     */
    fun register(registerBody: RegisterBody) {
        val username = registerBody.username!!
        val password = registerBody.password!!
        // 校验用户类型是否存在
        val userType = getUserType(registerBody.userType!!).userType
        val captchaEnabled = captchaProperties.enable!!
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, registerBody.code!!, registerBody.uuid!!)
        }
        val sysUser = SysUserBo()
        sysUser.userName = username
        sysUser.nickName = username
        sysUser.password = BCrypt.hashpw(password)
        sysUser.userType = userType
        if (!userService.checkUserNameUnique(sysUser)) {
            throw UserException("user.register.save.error", username)
        }
        val regFlag = userService.registerUser(sysUser)
        if (!regFlag) {
            throw UserException("user.register.error")
        }
        recordLogininfor(username, Constants.REGISTER, message("user.register.success"))
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    fun validateCaptcha(username: String, code: String, uuid: String) {
        val verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "")
        val captcha = getCacheObject<String>(verifyKey)
        deleteObject(verifyKey)
        if (captcha == null) {
            recordLogininfor(username, Constants.REGISTER, message("user.jcaptcha.expire"))
            throw CaptchaExpireException()
        }
        if (!code.equals(captcha, ignoreCase = true)) {
            recordLogininfor(username, Constants.REGISTER, message("user.jcaptcha.error"))
            throw CaptchaException()
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    private fun recordLogininfor(username: String, status: String, message: String) {
        val logininforEvent = LogininforEvent()
        logininforEvent.username = username
        logininforEvent.status = status
        logininforEvent.message = message
        logininforEvent.request = getRequest()
        context().publishEvent(logininforEvent)
    }
}
