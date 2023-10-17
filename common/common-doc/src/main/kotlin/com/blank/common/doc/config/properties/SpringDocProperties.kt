package com.blank.common.doc.config.properties

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.ExternalDocumentation
import io.swagger.v3.oas.models.Paths
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.tags.Tag
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.NestedConfigurationProperty

/**
 * swagger 配置属性
 */
@ConfigurationProperties(prefix = "springdoc")
class SpringDocProperties {
    /**
     * 文档基本信息
     */
    @NestedConfigurationProperty
    val info = SpringDocProperties.InfoProperties()

    /**
     * 扩展文档地址
     */
    @NestedConfigurationProperty
    val externalDocs: ExternalDocumentation? = null

    /**
     * 标签
     */
    val tags: List<Tag>? = null

    /**
     * 路径
     */
    @NestedConfigurationProperty
    val paths: Paths? = null

    /**
     * 组件
     */
    @NestedConfigurationProperty
    val components: Components? = null

    /**
     *
     *
     * 文档的基础属性信息
     *
     *
     * @see io.swagger.v3.oas.models.info.Info
     *
     *
     * 为了 springboot 自动生产配置提示信息，所以这里复制一个类出来
     */
    class InfoProperties {
        /**
         * 标题
         */
        val title: String? = null

        /**
         * 描述
         */
        val description: String? = null

        /**
         * 联系人信息
         */
        @NestedConfigurationProperty
        val contact: Contact? = null

        /**
         * 许可证
         */
        @NestedConfigurationProperty
        val license: License? = null

        /**
         * 版本
         */
        val version: String? = null
    }


}
