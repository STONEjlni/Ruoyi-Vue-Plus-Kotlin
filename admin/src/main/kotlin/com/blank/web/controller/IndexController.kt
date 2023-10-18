package com.blank.web.controller

import cn.dev33.satoken.annotation.SaIgnore
import com.blank.common.core.config.BlankConfig
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 首页
 */
@SaIgnore
@RestController
class IndexController(
    /**
     * 系统基础配置
     */
    private val blankConfig: BlankConfig
) {

    /**
     * 访问首页，提示语
     */
    @GetMapping("/")
    fun index(): String {
        return "欢迎使用${blankConfig.name!!}后台管理框架，当前版本：v${blankConfig.version!!}，请通过前端地址访问。"
    }
}
