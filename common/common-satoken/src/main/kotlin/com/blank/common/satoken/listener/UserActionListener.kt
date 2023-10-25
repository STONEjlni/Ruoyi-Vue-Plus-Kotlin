package com.blank.common.satoken.listener

import cn.dev33.satoken.config.SaTokenConfig
import cn.dev33.satoken.listener.SaTokenListener
import cn.dev33.satoken.stp.SaLoginModel
import cn.hutool.http.useragent.UserAgentUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.annotation.UserType
import com.blank.common.core.annotation.UserType.Companion.getUserType
import com.blank.common.core.constant.CacheConstants
import com.blank.common.core.domain.dto.UserOnlineDTO
import com.blank.common.core.utils.JakartaServletUtilExtend.getClientIP
import com.blank.common.core.utils.JakartaServletUtilExtend.getRequest
import com.blank.common.core.utils.ip.AddressUtils.getRealAddressByIP
import com.blank.common.redis.utils.RedisUtils.deleteObject
import com.blank.common.redis.utils.RedisUtils.setCacheObject
import com.blank.common.satoken.utils.LoginHelper
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * 用户行为 侦听器的实现
 */
@Component
@Slf4j
class UserActionListener(
    private val tokenConfig: SaTokenConfig
) : SaTokenListener {

    /**
     * 每次登录时触发
     */
    override fun doLogin(loginType: String, loginId: Any, tokenValue: String, loginModel: SaLoginModel) {
        val userType = getUserType(loginId.toString())
        if (userType === UserType.SYS_USER) {
            val userAgent = UserAgentUtil.parse(
                getRequest()!!.getHeader("User-Agent")
            )
            val ip = getClientIP()
            val user = LoginHelper.getLoginUser()!!
            val dto = UserOnlineDTO()
            dto.ipaddr = ip
            dto.loginLocation = getRealAddressByIP(ip)
            dto.browser = userAgent.browser.name
            dto.os = userAgent.os.name
            dto.loginTime = System.currentTimeMillis()
            dto.tokenId = tokenValue
            dto.userName = user.username
            dto.clientKey = user.clientKey
            dto.deviceType = user.deviceType
            dto.deptName = user.deptName
            if (tokenConfig.timeout == -1L) {
                setCacheObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue, dto)
            } else {
                setCacheObject(
                    CacheConstants.ONLINE_TOKEN_KEY + tokenValue,
                    dto,
                    Duration.ofSeconds(tokenConfig.timeout)
                )
            }
            log.info { "user doLogin, userId:$loginId, token:$tokenValue" }
        } else if (userType === UserType.APP_USER) {
            // app端 自行根据业务编写
        }
    }

    /**
     * 每次注销时触发
     */
    override fun doLogout(loginType: String, loginId: Any, tokenValue: String) {
        deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue)
        log.info { "user doLogout, userId:$loginId, token:$tokenValue" }
    }

    /**
     * 每次被踢下线时触发
     */
    override fun doKickout(loginType: String?, loginId: Any?, tokenValue: String) {
        deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue)
        log.info { "user doKickout, userId:$loginId, token:$tokenValue" }
    }

    /**
     * 每次被顶下线时触发
     */
    override fun doReplaced(loginType: String, loginId: Any, tokenValue: String) {
        deleteObject(CacheConstants.ONLINE_TOKEN_KEY + tokenValue)
        log.info { "user doReplaced, userId:$loginId, token:$tokenValue" }
    }

    /**
     * 每次被封禁时触发
     */
    override fun doDisable(loginType: String, loginId: Any, service: String, level: Int, disableTime: Long) {}

    /**
     * 每次被解封时触发
     */
    override fun doUntieDisable(loginType: String, loginId: Any, service: String) {}

    /**
     * 每次打开二级认证时触发
     */
    override fun doOpenSafe(loginType: String, tokenValue: String, service: String, safeTime: Long) {}

    /**
     * 每次创建Session时触发
     */
    override fun doCloseSafe(loginType: String, tokenValue: String, service: String) {}

    /**
     * 每次创建Session时触发
     */
    override fun doCreateSession(id: String) {}

    /**
     * 每次注销Session时触发
     */
    override fun doLogoutSession(id: String) {}

    /**
     * 每次Token续期时触发
     */
    override fun doRenewTimeout(tokenValue: String, loginId: Any, timeout: Long) {}

}
