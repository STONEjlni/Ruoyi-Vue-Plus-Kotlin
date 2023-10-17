package com.blank.common.encrypt.core

import com.blank.common.encrypt.enumd.AlgorithmType
import com.blank.common.encrypt.enumd.EncodeType

/**
 * 加密上下文 用于encryptor传递必要的参数。
 *
 */
class EncryptContext {
    /**
     * 默认算法
     */
    var algorithm: AlgorithmType? = null

    /**
     * 安全秘钥
     */
    var password: String? = null

    /**
     * 公钥
     */
    var publicKey: String? = null

    /**
     * 私钥
     */
    var privateKey: String? = null

    /**
     * 编码方式，base64/hex
     */
    var encode: EncodeType? = null
}
