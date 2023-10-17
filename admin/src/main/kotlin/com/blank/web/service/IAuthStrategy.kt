package com.blank.web.service

import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.domain.model.LoginBody
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.SpringUtilExtend.containsBean
import com.blank.system.domain.SysClient
import com.blank.web.domain.vo.LoginVo

/**
 * 授权策略
 */
interface IAuthStrategy {
    /**
     * 参数校验
     */
    fun validate(loginBody: LoginBody)

    /**
     * 登录
     */
    fun login(clientId: String, loginBody: LoginBody, client: SysClient): LoginVo?

    companion object {
        /**
         * 登录
         */
        @JvmStatic
        fun login(loginBody: LoginBody, client: SysClient): LoginVo? {
            // 授权类型和客户端id
            val clientId = loginBody.clientId!!
            val grantType = loginBody.grantType
            val beanName = grantType + BASE_NAME
            if (!containsBean(beanName)) {
                throw ServiceException("授权类型不正确!")
            }
            val instance = SpringUtil.getBean<IAuthStrategy>(beanName)
            instance.validate(loginBody)
            return instance.login(clientId, loginBody, client)
        }

        const val BASE_NAME = "AuthStrategy"
    }
}
