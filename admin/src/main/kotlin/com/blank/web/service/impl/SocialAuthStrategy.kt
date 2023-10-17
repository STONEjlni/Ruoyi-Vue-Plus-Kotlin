package com.blank.web.service.impl

import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.constant.Constants
import com.blank.common.core.domain.model.LoginBody
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.core.utils.ValidatorUtils.validate
import com.blank.common.core.validate.auth.SocialGroup
import com.blank.common.satoken.utils.LoginHelper
import com.blank.common.satoken.utils.LoginHelper.login
import com.blank.common.social.config.properties.SocialProperties
import com.blank.common.social.utils.SocialUtils.loginAuth
import com.blank.system.domain.SysClient
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysUserMapper
import com.blank.system.service.ISysSocialService
import com.blank.web.domain.vo.LoginVo
import com.blank.web.service.IAuthStrategy
import com.blank.web.service.SysLoginService
import org.springframework.stereotype.Service

/**
 * 第三方授权策略
 */
@Slf4j
@Service("social" + IAuthStrategy.BASE_NAME)
class SocialAuthStrategy(
    private val socialProperties: SocialProperties,
    private val sysSocialService: ISysSocialService,
    private val userMapper: SysUserMapper,
    private val loginService: SysLoginService
) : IAuthStrategy {

    override fun validate(loginBody: LoginBody) {
        validate(loginBody, SocialGroup::class.java)
    }

    /**
     * 登录-第三方授权登录
     *
     * @param clientId  客户端id
     * @param loginBody 登录信息
     * @param client    客户端信息
     */
    override fun login(clientId: String, loginBody: LoginBody, client: SysClient): LoginVo {
        val response = loginAuth(loginBody, socialProperties)
        if (!response.ok()) {
            throw ServiceException(response.msg)
        }
        val authUserData = response.data
        /*if ("GITEE".equals(authUserData.getSource())) {

        }*/
        val social = sysSocialService.selectByAuthId(authUserData.source + authUserData.uuid)
        if (!ObjectUtil.isNotNull(social)) {
            throw ServiceException("你还没有绑定第三方账号，绑定后才可以登录！")
        }

        // 查找用户
        val user = loadUser(social!!.userId)!!

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

    private fun loadUser(userId: Long?): SysUserVo? {
        /*SysUser user = userMapper.selectOne(new LambdaQueryWrapper<SysUser>()
            .select(SysUser::getUserName, SysUser::getStatus)
            .eq(SysUser::getUserId, userId));
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", "");
            throw new UserException("user.not.exists", "");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", "");
            throw new UserException("user.blocked", "");
        }
        return userMapper.selectUserByUserName(user.getUserName());*/
        return null
    }
}
