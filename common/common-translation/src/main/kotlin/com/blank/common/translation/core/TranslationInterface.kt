package com.blank.common.translation.core

/**
 * 翻译接口 (实现类需标注 {@link TranslationType} 注解标明翻译类型)
 */
interface TranslationInterface<T> {
    /**
     * 翻译
     *
     * @param key   需要被翻译的键(不为空)
     * @param other 其他参数
     * @return 返回键对应的值
     */
    fun translation(key: Any, other: String): T?
}
