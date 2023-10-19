package com.blank.web.controller

import cn.dev33.satoken.annotation.SaIgnore
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.domain.model.LoginBody
import com.blank.common.core.domain.model.RegisterBody
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.social.config.properties.SocialProperties
import com.blank.common.social.utils.SocialUtils.getAuthRequest
import com.blank.common.social.utils.SocialUtils.loginAuth
import com.blank.system.service.ISysClientService
import com.blank.system.service.ISysConfigService
import com.blank.system.service.ISysSocialService
import com.blank.web.domain.vo.LoginVo
import com.blank.web.service.IAuthStrategy
import com.blank.web.service.SysLoginService
import com.blank.web.service.SysRegisterService
import me.zhyd.oauth.utils.AuthStateUtils
import org.apache.commons.lang3.StringUtils
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 认证
 */
@Slf4j
@SaIgnore
@Validated
@RestController
@RequestMapping("/auth")
class AuthController(
    private val socialProperties: SocialProperties,
    private val loginService: SysLoginService,
    private val registerService: SysRegisterService,
    private val configService: ISysConfigService,
    private val socialUserService: ISysSocialService,
    private val clientService: ISysClientService
) {


    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @PostMapping("/login")
    fun login(@Validated @RequestBody loginBody: LoginBody): R<LoginVo> {
        // 授权类型和客户端id
        val clientId = loginBody.clientId
        val grantType = loginBody.grantType
        val client = clientService.queryByClientId(clientId!!)
        // 查询不到 client 或 client 内不包含 grantType
        if (ObjectUtil.isNull(client) || !StringUtils.contains(client!!.grantType, grantType)) {
            log.info { "客户端id: $clientId 认证类型：$grantType 异常!." }
            return fail(msg = message("auth.grant.type.error"))
        }
        // 登录
        return ok(data = IAuthStrategy.login(loginBody, client))
    }

    /**
     * 第三方登录请求
     *
     * @param source 登录来源
     * @return 结果
     */
    @GetMapping("/binding/{source}")
    fun authBinding(@PathVariable("source") source: String): R<String> {
        val obj = socialProperties.type!![source]
        if (ObjectUtil.isNull(obj)) {
            return fail(msg = "${source}平台账号暂不支持")
        }
        val authRequest = getAuthRequest(source, socialProperties)
        val authorizeUrl = authRequest.authorize(AuthStateUtils.createState())
        return ok(msg = "操作成功", data = authorizeUrl)
    }

    /**
     * 第三方登录回调业务处理 绑定授权
     *
     * @param loginBody 请求体
     * @return 结果
     */
    @PostMapping("/social/callback")
    fun socialCallback(@RequestBody loginBody: LoginBody): R<Void> {
        // 获取第三方登录信息
        val response = loginAuth(loginBody, socialProperties)
        val authUserData = response.data
        // 判断授权响应是否成功
        if (!response.ok()) {
            return fail(msg = response.msg)
        }
        loginService.socialRegister(authUserData)
        return ok()
    }

    /**
     * 取消授权
     *
     * @param socialId socialId
     */
    @DeleteMapping(value = ["/unlock/{socialId}"])
    fun unlockSocial(@PathVariable socialId: Long): R<Void> {
        val rows = socialUserService.deleteWithValidById(socialId)
        return if (rows) ok() else fail(msg = "取消授权失败")
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    fun logout(): R<Void> {
        loginService.logout()
        return ok(msg = "退出成功")
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    fun register(@Validated @RequestBody user: RegisterBody): R<Void> {
        registerService.register(user)
        return ok()
    }
}
