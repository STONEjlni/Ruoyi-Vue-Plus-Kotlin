package com.blank.monitor.admin.config

import de.codecentric.boot.admin.server.config.AdminServerProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

/**
 * admin 监控 安全配置
 */
@EnableWebSecurity
@Configuration
class SecurityConfig(adminServerProperties: AdminServerProperties) {
    private val adminContextPath: String

    init {
        adminContextPath = adminServerProperties.contextPath
    }

    @Bean
    @Throws(Exception::class)
    fun filterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        val successHandler = SavedRequestAwareAuthenticationSuccessHandler()
        successHandler.setTargetUrlParameter("redirectTo")
        successHandler.setDefaultTargetUrl("$adminContextPath/")
        return httpSecurity
            .headers { header: HeadersConfigurer<HttpSecurity?> ->
                header.frameOptions { it.disable() }
            }
            .authorizeHttpRequests { authorize: AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry ->
                authorize.requestMatchers(
                    AntPathRequestMatcher("$adminContextPath/assets/**"),
                    AntPathRequestMatcher("$adminContextPath/login"),
                    AntPathRequestMatcher("/actuator"),
                    AntPathRequestMatcher("/actuator/**")
                ).permitAll()
                    .anyRequest().authenticated()
            }
            .formLogin { formLogin: FormLoginConfigurer<HttpSecurity?> ->
                formLogin.loginPage(
                    "$adminContextPath/login"
                ).successHandler(successHandler)
            }
            .logout { logout: LogoutConfigurer<HttpSecurity?> ->
                logout.logoutUrl(
                    "$adminContextPath/logout"
                )
            }
            .httpBasic(Customizer.withDefaults<HttpBasicConfigurer<HttpSecurity>>())
            .csrf { obj: CsrfConfigurer<HttpSecurity> -> obj.disable() }
            .build()
    }
}

