package com.blank

import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

/**
 * web容器中进行部署
 */
class RuoyiVuePlusKotlinServletInitializer : SpringBootServletInitializer() {
    override fun configure(application: SpringApplicationBuilder): SpringApplicationBuilder {
        return application.sources(RuoyiVuePlusKotlinApplication::class.java)
    }
}
