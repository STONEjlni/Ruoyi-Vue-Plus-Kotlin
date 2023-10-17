package com.blank.common.encrypt.core.encryptor

import com.blank.common.encrypt.core.EncryptContext
import com.blank.common.encrypt.enumd.AlgorithmType
import com.blank.common.encrypt.enumd.EncodeType
import com.blank.common.encrypt.utils.EncryptUtils
import org.apache.commons.lang3.StringUtils

/**
 * RSA算法实现
 *
 */
class RsaEncryptor(
    private val context: EncryptContext
) : AbstractEncryptor(context) {
    init {
        val privateKey = context.privateKey
        val publicKey = context.publicKey
        require(!StringUtils.isAnyEmpty(privateKey, publicKey)) { "RSA公私钥均需要提供，公钥加密，私钥解密。" }
    }

    /**
     * 获得当前算法
     */
    override fun algorithm(): AlgorithmType {
        return AlgorithmType.RSA
    }

    /**
     * 加密
     *
     * @param value      待加密字符串
     * @param encodeType 加密后的编码格式
     */
    override fun encrypt(value: String?, encodeType: EncodeType?): String {
        return if (encodeType == EncodeType.HEX) {
            EncryptUtils.encryptByRsaHex(value, context.publicKey)
        } else {
            EncryptUtils.encryptByRsa(value, context.publicKey)
        }
    }

    /**
     * 解密
     *
     * @param value 待加密字符串
     */
    override fun decrypt(value: String?): String {
        return EncryptUtils.decryptByRsa(value, context.privateKey)
    }
}
