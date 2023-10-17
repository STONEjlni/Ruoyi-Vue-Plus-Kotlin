package com.blank.common.encrypt.core.encryptor

import com.blank.common.encrypt.core.EncryptContext
import com.blank.common.encrypt.core.IEncryptor

/**
 * 所有加密执行者的基类
 *
 */
abstract class AbstractEncryptor(
    context: EncryptContext
) : IEncryptor {

    init {
        // 用户配置校验与配置注入
    }

}
