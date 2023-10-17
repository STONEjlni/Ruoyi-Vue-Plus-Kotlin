package com.blank.common.core.exception.base

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.core.utils.MessageUtils
import org.apache.commons.lang3.StringUtils
import java.io.Serial

/**
 * 基础异常
 */
@Open
@NoArg
class BaseException @JvmOverloads constructor(
    /**
     * 所属模块
     */
    var module: String? = null,
    /**
     * 错误码
     */
    var code: String? = null,
    /**
     * 错误消息
     */
    var defaultMessage: String? = null,

    /**
     * 错误码对应的参数
     */
    vararg args: Any? = arrayOf(),
) : RuntimeException(defaultMessage) {
    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }

    /**
     * 错误码对应的参数
     */
    final var args: Array<Any>? = null

    override val message: String?
        get() {
            var message: String? = null
            if (!StringUtils.isEmpty(code!!)) {
                message = MessageUtils.message(code!!, this.args)
            }
            if (message == null) {
                message = defaultMessage
            }
            return message
        }

    init {
        this.args = arrayOf(args)
    }
}
