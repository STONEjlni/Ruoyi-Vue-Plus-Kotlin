package com.blank.common.core.annotation

/**
 * 设备类型
 * 针对一套 用户体系
 */
enum class DeviceType(val device: String) {
    /**
     * pc端
     */
    PC("pc"),

    /**
     * app端
     */
    APP("app"),

    /**
     * 小程序端
     */
    XCX("xcx"),

    /**
     * social第三方端
     */
    SOCIAL("social");
}
