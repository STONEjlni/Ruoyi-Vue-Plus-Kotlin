package com.blank.common.core.exception.user

import java.io.Serial

/**
 * 用户错误最大次数异常类
 */
class UserPasswordRetryLimitExceedException(
    retryLimitCount: Int, lockTime: Int
) : UserException(
    "user.password.retry.limit.exceed", args = arrayOf(retryLimitCount, lockTime)
) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
