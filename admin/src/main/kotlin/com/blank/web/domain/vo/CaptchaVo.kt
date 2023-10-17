package com.blank.web.domain.vo

/**
 * 验证码信息
 */
class CaptchaVo {
    /**
     * 是否开启验证码
     */
    var captchaEnabled = true
    var uuid: String? = null

    /**
     * 验证码图片
     */
    var img: String? = null
}
