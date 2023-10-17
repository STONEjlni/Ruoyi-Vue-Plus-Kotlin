package com.blank.common.security.handler

import cn.hutool.core.util.ReUtil
import cn.hutool.extra.spring.SpringUtil
import org.springframework.beans.factory.InitializingBean
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping
import org.springframework.web.util.pattern.PathPattern
import java.util.*
import java.util.function.Consumer
import java.util.regex.Pattern

/**
 * 获取所有Url配置
 */
class AllUrlHandler : InitializingBean {
    companion object {
        private val PATTERN = Pattern.compile("\\{(.*?)\\}")
    }

    val urls: MutableList<String> = mutableListOf()

    override fun afterPropertiesSet() {
        val set: MutableSet<String> = HashSet()
        val mapping = SpringUtil.getBean(
            "requestMappingHandlerMapping",
            RequestMappingHandlerMapping::class.java
        )
        val map = mapping.handlerMethods
        map.keys.forEach(Consumer {
            // 获取注解上边的 path 替代 path variable 为 *
            Objects.requireNonNull(
                it.pathPatternsCondition!!.patterns
            )
                .forEach(Consumer { url: PathPattern ->
                    set.add(
                        ReUtil.replaceAll(
                            url.patternString,
                            PATTERN,
                            "*"
                        )
                    )
                })
        })
        urls.addAll(set)
    }
}
