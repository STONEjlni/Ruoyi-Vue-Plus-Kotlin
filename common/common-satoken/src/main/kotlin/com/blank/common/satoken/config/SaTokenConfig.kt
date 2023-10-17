package com.blank.common.satoken.config

import cn.dev33.satoken.dao.SaTokenDao
import cn.dev33.satoken.jwt.StpLogicJwtForSimple
import cn.dev33.satoken.stp.StpInterface
import cn.dev33.satoken.stp.StpLogic
import com.blank.common.core.factory.YmlPropertySourceFactory
import com.blank.common.satoken.core.dao.PlusSaTokenDao
import com.blank.common.satoken.core.service.SaPermissionImpl
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.PropertySource
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * sa-token 配置
 */
@AutoConfiguration
@PropertySource(value = ["classpath:common-satoken.yml"], factory = YmlPropertySourceFactory::class)
class SaTokenConfig : WebMvcConfigurer {

    @Bean
    fun getStpLogicJwt(): StpLogic {
        // Sa-Token 整合 jwt (简单模式)
        return StpLogicJwtForSimple()
    }

    /**
     * 权限接口实现(使用bean注入方便用户替换)
     */
    @Bean
    fun stpInterface(): StpInterface {
        return SaPermissionImpl()
    }

    /**
     * 自定义dao层存储
     */
    @Bean
    fun saTokenDao(): SaTokenDao {
        return PlusSaTokenDao()
    }
}
