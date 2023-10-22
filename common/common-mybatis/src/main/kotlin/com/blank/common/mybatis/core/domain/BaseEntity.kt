package com.blank.common.mybatis.core.domain

import com.blank.common.core.annotation.Open
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.mybatisflex.annotation.Column
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * Entity基类
 */
@Open
class BaseEntity : Serializable {
    companion object {
        @Serial
        private const val serialVersionUID = 1L

        const val IS_DELETED_YES = "2"
        const val IS_DELETED_NO = "0"
    }

    /**
     * 搜索值
     */
    @JsonIgnore
    @Column(ignore = true)
    var searchValue: String? = null

    /**
     * 创建部门
     */
    var createDept: Long? = null

    /**
     * 创建者
     */
    var createBy: Long? = null

    /**
     * 创建时间
     */
    var createTime: Date? = null

    /**
     * 更新者
     */
    var updateBy: Long? = null

    /**
     * 更新时间
     */
    var updateTime: Date? = null

    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @Column(ignore = true)
    var params: MutableMap<String, Any> = HashMap()
}
