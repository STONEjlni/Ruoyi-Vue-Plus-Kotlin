package com.blank.web.service

import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.SpringUtilExtend.containsBean
import com.blank.system.domain.SysClient
import com.blank.web.domain.vo.LoginVo


/**
 * 授权策略
 */
interface IAuthStrategy {

    /**
     * 登录
     */
    fun login(clientId: String?, body: String?, client: SysClient?): LoginVo

    companion object {
        /**
         * 登录
         */
        fun login(body: String?, client: SysClient?, grantType: String?): LoginVo {
            // 授权类型和客户端id
            val clientId: String? = client?.clientId
            val beanName = grantType + BASE_NAME
            if (!containsBean(beanName)) {
                throw ServiceException("授权类型不正确!")
            }
            val instance = SpringUtil.getBean<IAuthStrategy>(beanName)
            return instance.login(clientId, body, client)
        }

        const val BASE_NAME = "AuthStrategy"
    }
}
