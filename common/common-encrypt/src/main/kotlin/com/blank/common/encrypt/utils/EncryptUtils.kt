package com.blank.common.encrypt.utils

import cn.hutool.core.codec.Base64
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.crypto.SecureUtil
import cn.hutool.crypto.SmUtil
import cn.hutool.crypto.asymmetric.KeyType
import java.nio.charset.StandardCharsets

/**
 * 安全相关工具类
 */
object EncryptUtils {
    /**
     * 公钥
     */
    const val PUBLIC_KEY = "publicKey"

    /**
     * 私钥
     */
    const val PRIVATE_KEY = "privateKey"

    /**
     * Base64加密
     *
     * @param data 待加密数据
     * @return 加密后字符串
     */
    @JvmStatic
    fun encryptByBase64(data: String?): String {
        return Base64.encode(data, StandardCharsets.UTF_8)
    }

    /**
     * Base64解密
     *
     * @param data 待解密数据
     * @return 解密后字符串
     */
    @JvmStatic
    fun decryptByBase64(data: String?): String {
        return Base64.decodeStr(data, StandardCharsets.UTF_8)
    }

    /**
     * AES加密
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Base64编码
     */
    @JvmStatic
    fun encryptByAes(data: String?, password: String): String {
        require(!StrUtil.isBlank(password)) { "AES需要传入秘钥信息" }
        // aes算法的秘钥要求是16位、24位、32位
        val array = intArrayOf(16, 24, 32)
        require(ArrayUtil.contains(array, password.length)) { "AES秘钥长度要求为16位、24位、32位" }
        return SecureUtil.aes(password.toByteArray(StandardCharsets.UTF_8)).encryptBase64(data, StandardCharsets.UTF_8)
    }

    /**
     * AES加密
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Hex编码
     */
    @JvmStatic
    fun encryptByAesHex(data: String?, password: String): String {
        require(!StrUtil.isBlank(password)) { "AES需要传入秘钥信息" }
        // aes算法的秘钥要求是16位、24位、32位
        val array = intArrayOf(16, 24, 32)
        require(ArrayUtil.contains(array, password.length)) { "AES秘钥长度要求为16位、24位、32位" }
        return SecureUtil.aes(password.toByteArray(StandardCharsets.UTF_8)).encryptHex(data, StandardCharsets.UTF_8)
    }

    /**
     * AES解密
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 解密后字符串
     */
    @JvmStatic
    fun decryptByAes(data: String?, password: String): String {
        require(!StrUtil.isBlank(password)) { "AES需要传入秘钥信息" }
        // aes算法的秘钥要求是16位、24位、32位
        val array = intArrayOf(16, 24, 32)
        require(ArrayUtil.contains(array, password.length)) { "AES秘钥长度要求为16位、24位、32位" }
        return SecureUtil.aes(password.toByteArray(StandardCharsets.UTF_8)).decryptStr(data, StandardCharsets.UTF_8)
    }

    /**
     * sm4加密
     *
     * @param data     待加密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Base64编码
     */
    @JvmStatic
    fun encryptBySm4(data: String?, password: String): String {
        require(!StrUtil.isBlank(password)) { "SM4需要传入秘钥信息" }
        // sm4算法的秘钥要求是16位长度
        val sm4PasswordLength = 16
        require(sm4PasswordLength == password.length) { "SM4秘钥长度要求为16位" }
        return SmUtil.sm4(password.toByteArray(StandardCharsets.UTF_8)).encryptBase64(data, StandardCharsets.UTF_8)
    }

    /**
     * sm4加密
     *
     * @param data     待加密数据
     * @param password 秘钥字符串
     * @return 加密后字符串, 采用Base64编码
     */
    @JvmStatic
    fun encryptBySm4Hex(data: String?, password: String): String {
        require(!StrUtil.isBlank(password)) { "SM4需要传入秘钥信息" }
        // sm4算法的秘钥要求是16位长度
        val sm4PasswordLength = 16
        require(sm4PasswordLength == password.length) { "SM4秘钥长度要求为16位" }
        return SmUtil.sm4(password.toByteArray(StandardCharsets.UTF_8)).encryptHex(data, StandardCharsets.UTF_8)
    }

    /**
     * sm4解密
     *
     * @param data     待解密数据
     * @param password 秘钥字符串
     * @return 解密后字符串
     */
    @JvmStatic
    fun decryptBySm4(data: String?, password: String): String {
        require(!StrUtil.isBlank(password)) { "SM4需要传入秘钥信息" }
        // sm4算法的秘钥要求是16位长度
        val sm4PasswordLength = 16
        require(sm4PasswordLength == password.length) { "SM4秘钥长度要求为16位" }
        return SmUtil.sm4(password.toByteArray(StandardCharsets.UTF_8)).decryptStr(data, StandardCharsets.UTF_8)
    }

    /**
     * 产生sm2加解密需要的公钥和私钥
     *
     * @return 公私钥Map
     */
    @JvmStatic
    fun generateSm2Key(): Map<String, String> {
        val keyMap: MutableMap<String, String> = HashMap(2)
        val sm2 = SmUtil.sm2()
        keyMap[PRIVATE_KEY] = sm2.privateKeyBase64
        keyMap[PUBLIC_KEY] = sm2.publicKeyBase64
        return keyMap
    }

    /**
     * sm2公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Base64编码
     */
    @JvmStatic
    fun encryptBySm2(data: String?, publicKey: String?): String {
        require(!StrUtil.isBlank(publicKey)) { "SM2需要传入公钥进行加密" }
        val sm2 = SmUtil.sm2(null, publicKey)
        return sm2.encryptBase64(data, StandardCharsets.UTF_8, KeyType.PublicKey)
    }

    /**
     * sm2公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Hex编码
     */
    @JvmStatic
    fun encryptBySm2Hex(data: String?, publicKey: String?): String {
        require(!StrUtil.isBlank(publicKey)) { "SM2需要传入公钥进行加密" }
        val sm2 = SmUtil.sm2(null, publicKey)
        return sm2.encryptHex(data, StandardCharsets.UTF_8, KeyType.PublicKey)
    }

    /**
     * sm2私钥解密
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 解密后字符串
     */
    @JvmStatic
    fun decryptBySm2(data: String?, privateKey: String?): String {
        require(!StrUtil.isBlank(privateKey)) { "SM2需要传入私钥进行解密" }
        val sm2 = SmUtil.sm2(privateKey, null)
        return sm2.decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8)
    }

    /**
     * 产生RSA加解密需要的公钥和私钥
     *
     * @return 公私钥Map
     */
    @JvmStatic
    fun generateRsaKey(): Map<String, String> {
        val keyMap: MutableMap<String, String> = HashMap(2)
        val rsa = SecureUtil.rsa()
        keyMap[PRIVATE_KEY] = rsa.privateKeyBase64
        keyMap[PUBLIC_KEY] = rsa.publicKeyBase64
        return keyMap
    }

    /**
     * rsa公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Base64编码
     */
    @JvmStatic
    fun encryptByRsa(data: String?, publicKey: String?): String {
        require(!StrUtil.isBlank(publicKey)) { "RSA需要传入公钥进行加密" }
        val rsa = SecureUtil.rsa(null, publicKey)
        return rsa.encryptBase64(data, StandardCharsets.UTF_8, KeyType.PublicKey)
    }

    /**
     * rsa公钥加密
     *
     * @param data      待加密数据
     * @param publicKey 公钥
     * @return 加密后字符串, 采用Hex编码
     */
    @JvmStatic
    fun encryptByRsaHex(data: String?, publicKey: String?): String {
        require(!StrUtil.isBlank(publicKey)) { "RSA需要传入公钥进行加密" }
        val rsa = SecureUtil.rsa(null, publicKey)
        return rsa.encryptHex(data, StandardCharsets.UTF_8, KeyType.PublicKey)
    }

    /**
     * rsa私钥解密
     *
     * @param data       待加密数据
     * @param privateKey 私钥
     * @return 解密后字符串
     */
    @JvmStatic
    fun decryptByRsa(data: String?, privateKey: String?): String {
        require(!StrUtil.isBlank(privateKey)) { "RSA需要传入私钥进行解密" }
        val rsa = SecureUtil.rsa(privateKey, null)
        return rsa.decryptStr(data, KeyType.PrivateKey, StandardCharsets.UTF_8)
    }

    /**
     * md5加密
     *
     * @param data 待加密数据
     * @return 加密后字符串, 采用Hex编码
     */
    @JvmStatic
    fun encryptByMd5(data: String?): String {
        return SecureUtil.md5(data)
    }

    /**
     * sha256加密
     *
     * @param data 待加密数据
     * @return 加密后字符串, 采用Hex编码
     */
    @JvmStatic
    fun encryptBySha256(data: String?): String {
        return SecureUtil.sha256(data)
    }

    /**
     * sm3加密
     *
     * @param data 待加密数据
     * @return 加密后字符串, 采用Hex编码
     */
    @JvmStatic
    fun encryptBySm3(data: String?): String {
        return SmUtil.sm3(data)
    }
}
