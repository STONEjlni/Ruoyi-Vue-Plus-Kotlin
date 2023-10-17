package com.blank.common.core.factory

import cn.hutool.core.util.StrUtil
import org.apache.commons.lang3.StringUtils
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.DefaultPropertySourceFactory
import org.springframework.core.io.support.EncodedResource
import java.io.IOException

/**
 * yml 配置源工厂
 */
class YmlPropertySourceFactory : DefaultPropertySourceFactory() {
    @Throws(IOException::class)
    override fun createPropertySource(name: String?, resource: EncodedResource): PropertySource<*> {
        val sourceName = resource.resource.filename
        if (StrUtil.isNotBlank(sourceName) && StringUtils.endsWithAny(sourceName, ".yml", ".yaml")) {
            val factory = YamlPropertiesFactoryBean()
            factory.setResources(resource.resource)
            factory.afterPropertiesSet()
            return PropertiesPropertySource(sourceName!!, factory.getObject()!!)
        }
        return super.createPropertySource(name, resource)
    }
}
