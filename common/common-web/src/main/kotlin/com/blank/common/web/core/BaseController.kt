package com.blank.common.web.core

import com.blank.common.core.annotation.Open
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.utils.StringUtilsExtend.format

/**
 * web层通用数据处理
 */
@Open
class BaseController {
    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    fun toAjax(rows: Int): R<Unit> {
        return if (rows > 0) ok() else fail()
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    fun toAjax(result: Boolean): R<Unit> {
        return if (result) ok() else fail()
    }

    /**
     * 页面跳转
     */
    fun redirect(url: String?): String {
        return format("redirect:{}", url!!)
    }
}
