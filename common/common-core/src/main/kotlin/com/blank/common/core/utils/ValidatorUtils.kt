package com.blank.common.core.utils

import cn.hutool.extra.spring.SpringUtil
import jakarta.validation.ConstraintViolationException
import jakarta.validation.Validator

/**
 * Validator 校验框架工具
 */
object ValidatorUtils {
    private val VALID: Validator = SpringUtil.getBean(Validator::class.java)

    @JvmStatic
    fun <T> validate(`object`: T, vararg groups: Class<*>?) {
        val validate = VALID.validate(`object`, *groups)
        if (validate.isNotEmpty()) {
            throw ConstraintViolationException("参数校验异常", validate)
        }
    }
}
