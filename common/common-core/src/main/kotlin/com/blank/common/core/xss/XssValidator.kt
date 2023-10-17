package com.blank.common.core.xss

import cn.hutool.core.util.ReUtil
import cn.hutool.http.HtmlUtil
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

/**
 * 自定义xss校验注解实现
 */
class XssValidator : ConstraintValidator<Xss, String> {
    override fun isValid(value: String?, constraintValidatorContext: ConstraintValidatorContext?): Boolean {
        return !ReUtil.contains(HtmlUtil.RE_HTML_MARK, value)
    }
}
