package com.blank.web.service.impl

import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.annotation.LoginType
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.annotation.UserStatus
import com.blank.common.core.constant.Constants
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.model.LoginBody
import com.blank.common.core.exception.user.CaptchaExpireException
import com.blank.common.core.exception.user.UserException
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.core.utils.ValidatorUtils.validate
import com.blank.common.core.validate.auth.EmailGroup
import com.blank.common.redis.utils.RedisUtils.getCacheObject
import com.blank.common.satoken.utils.LoginHelper
import com.blank.common.satoken.utils.LoginHelper.login
import com.blank.system.domain.SysClient
import com.blank.system.domain.table.SysUserDef
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysUserMapper
import com.blank.web.domain.vo.LoginVo
import com.blank.web.service.IAuthStrategy
import com.blank.web.service.SysLoginService
import com.mybatisflex.core.query.QueryWrapper
import org.springframework.stereotype.Service

/**
 * 邮件认证策略
 */
@Slf4j
@Service("email" + IAuthStrategy.BASE_NAME)
class EmailAuthStrategy(
    private val loginService: SysLoginService,
    private val userMapper: SysUserMapper
) : IAuthStrategy {

    override fun validate(loginBody: LoginBody) {
        validate(loginBody, EmailGroup::class.java)
    }

    override fun login(clientId: String, loginBody: LoginBody, client: SysClient): LoginVo {
        val email = loginBody.email!!
        val emailCode = loginBody.emailCode!!

        // 通过邮箱查找用户
        val user = loadUserByEmail(email)
        loginService.checkLogin(LoginType.EMAIL, user!!.userName!!) { !validateEmailCode(email, emailCode) }
        // 此处可根据登录用户的数据不同 自行创建 loginUser 属性不够用继承扩展就行了
        val loginUser = loginService.buildLoginUser(user)
        val model = SaLoginModel()
        model.setDevice(client.deviceType)
        // 自定义分配 不同用户体系 不同 token 授权时间 不设置默认走全局 yml 配置
        // 例如: 后台用户30分钟过期 app用户1天过期
        model.setTimeout(client.timeout!!)
        model.setActiveTimeout(client.activeTimeout!!)
        model.setExtra(LoginHelper.CLIENT_KEY, clientId)
        // 生成token
        login(loginUser, model)
        loginService.recordLogininfor(user.userName!!, Constants.LOGIN_SUCCESS, message("user.login.success"))
        loginService.recordLoginInfo(user.userId!!)
        val loginVo = LoginVo()
        loginVo.accessToken = StpUtil.getTokenValue()
        loginVo.expireIn = StpUtil.getTokenTimeout()
        loginVo.clientId = clientId
        return loginVo
    }

    /**
     * 校验邮箱验证码
     */
    private fun validateEmailCode(email: String, emailCode: String): Boolean {
        val code = getCacheObject<String>(GlobalConstants.CAPTCHA_CODE_KEY + email)
        if (StrUtil.isBlank(code)) {
            loginService.recordLogininfor(email, Constants.LOGIN_FAIL, message("user.jcaptcha.expire"))
            throw CaptchaExpireException()
        }
        return code == emailCode
    }

    private fun loadUserByEmail(email: String): SysUserVo? {
        val def = SysUserDef.SYS_USER
        var user = userMapper.selectOneByQuery(
            QueryWrapper()
                .select(def.EMAIL, def.STATUS)
                .where(def.EMAIL.eq(email))
        )

        if (ObjectUtil.isNull(user)) {
            log.info { "登录用户：$email 不存在." }
            throw UserException("user.not.exists", email)
        } else if (UserStatus.DISABLE.code == user.status) {
            log.info { "登录用户：$email 已被停用." }
            throw UserException("user.blocked", email)
        }

        return userMapper.selectUserByEmail(email)
    }
}
