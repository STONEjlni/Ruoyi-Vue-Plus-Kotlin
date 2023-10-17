package com.blank.common.encrypt.core

import com.blank.common.encrypt.enumd.AlgorithmType
import com.blank.common.encrypt.enumd.EncodeType

/**
 * 加解者
 *
 */
interface IEncryptor {
    /**
     * 获得当前算法
     */
    fun algorithm(): AlgorithmType?

    /**
     * 加密
     *
     * @param value      待加密字符串
     * @param encodeType 加密后的编码格式
     * @return 加密后的字符串
     */
    fun encrypt(value: String?, encodeType: EncodeType?): String?

    /**
     * 解密
     *
     * @param value 待加密字符串
     * @return 解密后的字符串
     */
    fun decrypt(value: String?): String?
}
