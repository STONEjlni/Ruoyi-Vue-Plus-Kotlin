package com.blank.common.translation.core.impl

import cn.hutool.core.util.StrUtil
import com.blank.common.core.service.DictService
import com.blank.common.translation.annotation.TranslationType
import com.blank.common.translation.constant.TransConstant
import com.blank.common.translation.core.TranslationInterface

/**
 * 字典翻译实现
 */
@TranslationType(type = TransConstant.DICT_TYPE_TO_LABEL)
class DictTypeTranslationImpl(
    private val dictService: DictService
) : TranslationInterface<String> {
    override fun translation(key: Any, other: String): String? {
        return if (key is String && StrUtil.isNotBlank(other)) {
            dictService.getDictLabel(other, key)
        } else null
    }
}
