package com.blank.common.web.config

import com.blank.common.web.interceptor.PlusWebInvokeTimeInterceptor
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * 通用配置
 */
@AutoConfiguration
class ResourcesConfig : WebMvcConfigurer {
    override fun addInterceptors(registry: InterceptorRegistry) {
        // 全局访问性能拦截
        registry.addInterceptor(PlusWebInvokeTimeInterceptor())
    }

    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {}

    /**
     * 跨域配置
     */
    @Bean
    fun corsFilter(): CorsFilter {
        val config = CorsConfiguration()
        config.allowCredentials = true
        // 设置访问源地址
        config.addAllowedOriginPattern("*")
        // 设置访问源请求头
        config.addAllowedHeader("*")
        // 设置访问源请求方法
        config.addAllowedMethod("*")
        // 有效期 1800秒
        config.maxAge = 1800L
        // 添加映射路径，拦截一切请求
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", config)
        // 返回新的CorsFilter
        return CorsFilter(source)
    }
}
