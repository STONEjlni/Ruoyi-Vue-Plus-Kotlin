package com.blank.web.service.impl

import cn.dev33.satoken.secure.BCrypt
import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.LoginType
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.annotation.UserStatus
import com.blank.common.core.constant.Constants
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.model.LoginBody
import com.blank.common.core.exception.user.CaptchaException
import com.blank.common.core.exception.user.CaptchaExpireException
import com.blank.common.core.exception.user.UserException
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.core.utils.ValidatorUtils.validate
import com.blank.common.core.validate.auth.PasswordGroup
import com.blank.common.redis.utils.RedisUtils.deleteObject
import com.blank.common.redis.utils.RedisUtils.getCacheObject
import com.blank.common.satoken.utils.LoginHelper
import com.blank.common.satoken.utils.LoginHelper.login
import com.blank.common.web.config.properties.CaptchaProperties
import com.blank.system.domain.SysClient
import com.blank.system.domain.table.SysUserDef
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysUserMapper
import com.blank.web.domain.vo.LoginVo
import com.blank.web.service.IAuthStrategy
import com.blank.web.service.SysLoginService
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

/**
 * 密码认证策略
 */
@Slf4j
@Service("password" + IAuthStrategy.BASE_NAME)
class PasswordAuthStrategy(
    private val captchaProperties: CaptchaProperties,
    private val loginService: SysLoginService,
    private val userMapper: SysUserMapper
) : IAuthStrategy {

    override fun validate(loginBody: LoginBody) {
        validate(loginBody, PasswordGroup::class.java)
    }

    override fun login(clientId: String, loginBody: LoginBody, client: SysClient): LoginVo {
        val username = loginBody.username!!
        val password = loginBody.password!!
        val code = loginBody.code!!
        val uuid = loginBody.uuid!!
        val captchaEnabled = captchaProperties.enable!!
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid)
        }
        val user = loadUserByUsername(username)
        loginService.checkLogin(LoginType.PASSWORD, username) { !BCrypt.checkpw(password, user.password) }
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        val loginUser = loginService.buildLoginUser(user)
        loginUser.clientKey = client.clientKey
        loginUser.deviceType = client.deviceType
        val model = SaLoginModel()
        model.setDevice(client.deviceType)
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.timeout!!)
        model.setActiveTimeout(client.activeTimeout!!)
        model.setExtra(LoginHelper.CLIENT_KEY, clientId)
        // 生成token
        login(loginUser, model)
        loginService.recordLogininfor(username, Constants.LOGIN_SUCCESS, message("user.login.success"))
        loginService.recordLoginInfo(user.userId!!)
        val loginVo = LoginVo()
        loginVo.accessToken = StpUtil.getTokenValue()
        loginVo.expireIn = StpUtil.getTokenTimeout()
        loginVo.clientId = clientId
        return loginVo
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    private fun validateCaptcha(username: String, code: String, uuid: String) {
        val verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "")
        val captcha = getCacheObject<String>(verifyKey)
        deleteObject(verifyKey)
        if (captcha == null) {
            loginService.recordLogininfor(username, Constants.LOGIN_FAIL, message("user.jcaptcha.expire"))
            throw CaptchaExpireException()
        }
        if (!code.equals(captcha, ignoreCase = true)) {
            loginService.recordLogininfor(username, Constants.LOGIN_FAIL, message("user.jcaptcha.error"))
            throw CaptchaException()
        }
    }

    private fun loadUserByUsername(username: String): SysUserVo {
        val def = SysUserDef.SYS_USER
        val user = userMapper.selectOneByQuery(
            QueryWrapper()
                .select(def.USER_NAME, def.STATUS)
                .where(def.USER_NAME.eq(username)))
        if (ObjectUtil.isNull(user)) {
            log.info { "登录用户：$username 不存在." }
            throw UserException("user.not.exists", username)
        } else if (UserStatus.DISABLE.code == user.status) {
            log.info { "登录用户：$username 已被停用." }
            throw UserException("user.blocked", username)
        }
        return userMapper.selectUserByUserName(username)
    }
}
