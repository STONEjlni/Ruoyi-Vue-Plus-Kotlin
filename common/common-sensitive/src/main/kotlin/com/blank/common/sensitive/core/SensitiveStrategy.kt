package com.blank.common.sensitive.core

import cn.hutool.core.util.DesensitizedUtil
import java.util.function.Function

/**
 * 脱敏策略
 *
 */
enum class SensitiveStrategy(
    //可自行添加其他脱敏策略
    val desensitizer: Function<String, String>? = null
) {
    /**
     * 身份证脱敏
     */
    ID_CARD({ DesensitizedUtil.idCardNum(it, 3, 4) }),

    /**
     * 手机号脱敏
     */
    PHONE(DesensitizedUtil::mobilePhone),

    /**
     * 地址脱敏
     */
    ADDRESS({ DesensitizedUtil.address(it, 8) }),

    /**
     * 邮箱脱敏
     */
    EMAIL(DesensitizedUtil::email),

    /**
     * 银行卡
     */
    BANK_CARD(DesensitizedUtil::bankCard);
}
