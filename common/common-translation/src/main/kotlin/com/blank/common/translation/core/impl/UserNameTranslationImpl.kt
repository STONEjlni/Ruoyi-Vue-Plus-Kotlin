package com.blank.common.translation.core.impl

import com.blank.common.core.service.UserService
import com.blank.common.translation.annotation.TranslationType
import com.blank.common.translation.constant.TransConstant
import com.blank.common.translation.core.TranslationInterface

/**
 * 用户名翻译实现
 */
@TranslationType(type = TransConstant.USER_ID_TO_NAME)
class UserNameTranslationImpl(
    private val userService: UserService
) : TranslationInterface<String> {

    override fun translation(key: Any, other: String): String? {
        return if (key is Long) {
            userService.selectUserNameById(key)
        } else null
    }
}
