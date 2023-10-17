package com.blank.common.translation.core.impl

import com.blank.common.core.service.OssService
import com.blank.common.translation.annotation.TranslationType
import com.blank.common.translation.constant.TransConstant
import com.blank.common.translation.core.TranslationInterface

/**
 * OSS翻译实现
 */
@TranslationType(type = TransConstant.OSS_ID_TO_URL)
class OssUrlTranslationImpl(
    private var ossService: OssService
) : TranslationInterface<String> {
    override fun translation(key: Any, other: String): String? {
        if (key is String) {
            return ossService.selectUrlByIds(key)
        } else if (key is Long) {
            return ossService.selectUrlByIds(key.toString())
        }
        return null
    }
}
