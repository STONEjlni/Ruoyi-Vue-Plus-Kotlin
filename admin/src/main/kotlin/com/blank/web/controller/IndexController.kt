package com.blank.web.controller

import cn.dev33.satoken.annotation.SaIgnore
import com.blank.common.core.config.BlankConfig
import com.blank.common.core.utils.StringUtilsExtend.format
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
        return format(
            "欢迎使用{}后台管理框架，当前版本：v{}，请通过前端地址访问。",
            blankConfig.name!!,
            blankConfig.version!!
        )
    }
}
