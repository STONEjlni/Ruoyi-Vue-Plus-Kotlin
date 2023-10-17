package com.blank.common.web.config

import cn.hutool.captcha.CaptchaUtil
import cn.hutool.captcha.CircleCaptcha
import cn.hutool.captcha.LineCaptcha
import cn.hutool.captcha.ShearCaptcha
import com.blank.common.web.config.properties.CaptchaProperties
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Lazy
import java.awt.Color
import java.awt.Font

/**
 * 验证码配置
 */
@AutoConfiguration
@EnableConfigurationProperties(CaptchaProperties::class)
class CaptchaConfig {
    companion object {
        private const val WIDTH = 160
        private const val HEIGHT = 60
        private val BACKGROUND = Color.PINK
        private val FONT = Font("Arial", Font.BOLD, 48)
    }

    /**
     * 圆圈干扰验证码
     */
    @Lazy
    @Bean
    fun circleCaptcha(): CircleCaptcha {
        val captcha = CaptchaUtil.createCircleCaptcha(WIDTH, HEIGHT)
        captcha.setBackground(BACKGROUND)
        captcha.setFont(FONT)
        return captcha
    }

    /**
     * 线段干扰的验证码
     */
    @Lazy
    @Bean
    fun lineCaptcha(): LineCaptcha {
        val captcha = CaptchaUtil.createLineCaptcha(WIDTH, HEIGHT)
        captcha.setBackground(BACKGROUND)
        captcha.setFont(FONT)
        return captcha
    }

    /**
     * 扭曲干扰验证码
     */
    @Lazy
    @Bean
    fun shearCaptcha(): ShearCaptcha {
        val captcha = CaptchaUtil.createShearCaptcha(WIDTH, HEIGHT)
        captcha.setBackground(BACKGROUND)
        captcha.setFont(FONT)
        return captcha
    }
}
