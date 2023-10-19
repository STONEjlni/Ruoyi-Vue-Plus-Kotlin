package com.blank.common.web.config.properties

import com.blank.common.web.enums.CaptchaCategory
import com.blank.common.web.enums.CaptchaType
import org.springframework.boot.context.properties.ConfigurationProperties

/**
 * 验证码 配置属性
 */
@ConfigurationProperties(prefix = "captcha")
class CaptchaProperties {
    var enable: Boolean? = null

    /**
     * 验证码类型
     */
    var type: CaptchaType? = null

    /**
     * 验证码类别
     */
    var category: CaptchaCategory? = null

    /**
     * 数字验证码位数
     */
    var numberLength: Int? = null

    /**
     * 字符验证码长度
     */
    var charLength: Int? = null
}
