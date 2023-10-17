package com.blank.common.oss.enumd

/**
 * minio策略配置
 */
enum class PolicyType(
    /**
     * 类型
     */
    private val type: String
) {
    /**
     * 只读
     */
    READ("read-only"),

    /**
     * 只写
     */
    WRITE("write-only"),

    /**
     * 读写
     */
    READ_WRITE("read-write");
}
