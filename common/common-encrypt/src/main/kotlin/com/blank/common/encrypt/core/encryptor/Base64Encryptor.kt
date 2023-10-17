package com.blank.common.encrypt.core.encryptor

import com.blank.common.encrypt.core.EncryptContext
import com.blank.common.encrypt.enumd.AlgorithmType
import com.blank.common.encrypt.enumd.EncodeType
import com.blank.common.encrypt.utils.EncryptUtils

/**
 * Base64算法实现
 *
 */
class Base64Encryptor(
    private val context: EncryptContext
) : AbstractEncryptor(context) {

    /**
     * 获得当前算法
     */
    override fun algorithm(): AlgorithmType {
        return AlgorithmType.BASE64
    }

    /**
     * 加密
     *
     * @param value      待加密字符串
     * @param encodeType 加密后的编码格式
     */
    override fun encrypt(value: String?, encodeType: EncodeType?): String {
        return EncryptUtils.encryptByBase64(value)
    }

    /**
     * 解密
     *
     * @param value 待加密字符串
     */
    override fun decrypt(value: String?): String {
        return EncryptUtils.decryptByBase64(value)
    }
}
