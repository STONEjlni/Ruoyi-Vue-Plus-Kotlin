package com.blank.common.core.annotation

/**
 * 用户状态
 */
enum class UserStatus(
    val code: String,
    val info: String
) {
    /**
     * 正常
     */
    OK("0", "正常"),

    /**
     * 停用
     */
    DISABLE("1", "停用"),

    /**
     * 删除
     */
    DELETED("2", "删除");
}
