package com.blank.web.controller

import cn.dev33.satoken.annotation.SaIgnore
import cn.hutool.captcha.generator.CodeGenerator
import cn.hutool.core.util.IdUtil
import cn.hutool.core.util.RandomUtil
import cn.hutool.core.util.ReflectUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.constant.Constants
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.mail.config.properties.MailProperties
import com.blank.common.mail.utils.MailUtils.sendText
import com.blank.common.redis.utils.RedisUtils.setCacheObject
import com.blank.common.web.config.properties.CaptchaProperties
import com.blank.common.web.enums.CaptchaType
import com.blank.web.domain.vo.CaptchaVo
import jakarta.validation.constraints.NotBlank
import org.apache.commons.lang3.StringUtils
import org.springframework.expression.ExpressionParser
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Duration

/**
 * 验证码操作处理
 */
@SaIgnore
@Slf4j
@Validated
@RestController
class CaptchaController(
    private val captchaProperties: CaptchaProperties,
    private val mailProperties: MailProperties
) {


    /**
     * 邮箱验证码
     *
     * @param email 邮箱
     */
    @GetMapping("/resource/email/code")
    fun emailCode(email: @NotBlank(message = "{user.email.not.blank}") String): R<Unit> {
        val key = GlobalConstants.CAPTCHA_CODE_KEY + email
        val code = RandomUtil.randomNumbers(4)
        setCacheObject(key, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION.toLong()))
        try {
            sendText(
                email,
                "登录验证码",
                "您本次验证码为：" + code + "，有效性为" + Constants.CAPTCHA_EXPIRATION + "分钟，请尽快填写。"
            )
        } catch (e: Exception) {
            log.error { "验证码短信发送异常 => ${e.message}" }
            return fail(msg = e.message)
        }
        return ok()
    }

    /**
     * 生成验证码
     */
    @GetMapping("/auth/code")
    fun getCode(): R<CaptchaVo> {
        val captchaVo = CaptchaVo()
        val captchaEnabled = captchaProperties.enable!!
        if (!captchaEnabled) {
            captchaVo.captchaEnabled = false
            return ok(data = captchaVo)
        }
        // 保存验证码信息
        val uuid = IdUtil.simpleUUID()
        val verifyKey = GlobalConstants.CAPTCHA_CODE_KEY + uuid
        // 生成验证码
        val captchaType = captchaProperties.type
        val isMath = CaptchaType.MATH === captchaType
        val length = if (isMath) captchaProperties.numberLength else captchaProperties.charLength
        val codeGenerator: CodeGenerator = ReflectUtil.newInstance(
            captchaType!!.clazz, length
        )
        val captcha = SpringUtil.getBean(captchaProperties.category!!.clazz)
        captcha.generator = codeGenerator
        captcha.createCode()
        var code = captcha.code
        if (isMath) {
            val parser: ExpressionParser = SpelExpressionParser()
            val exp = parser.parseExpression(StringUtils.remove(code, "="))
            code = exp.getValue(String::class.java)
        }
        setCacheObject(verifyKey, code, Duration.ofMinutes(Constants.CAPTCHA_EXPIRATION.toLong()))
        captchaVo.uuid = uuid
        captchaVo.img = captcha.imageBase64
        return ok(data = captchaVo)
    }
}
