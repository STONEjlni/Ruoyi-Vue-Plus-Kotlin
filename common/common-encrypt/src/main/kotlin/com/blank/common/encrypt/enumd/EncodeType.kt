package com.blank.common.encrypt.enumd

/**
 * 编码类型
 *
 */
enum class EncodeType {
    /**
     * 默认使用yml配置
     */
    DEFAULT,

    /**
     * base64编码
     */
    BASE64,

    /**
     * 16进制编码
     */
    HEX;
}
