package com.blank.common.core.annotation

import org.apache.commons.lang3.StringUtils

/**
 * 设备类型
 * 针对多套 用户体系
 */
enum class UserType(
    val userType: String
) {
    /**
     * pc端
     */
    SYS_USER("sys_user"),

    /**
     * app端
     */
    APP_USER("app_user");

    companion object {
        @JvmStatic
        fun getUserType(str: String): UserType {
            for (value in entries) {
                if (StringUtils.contains(str, value.userType)) {
                    return value
                }
            }
            throw RuntimeException("'UserType' not found By $str")
        }
    }
}
