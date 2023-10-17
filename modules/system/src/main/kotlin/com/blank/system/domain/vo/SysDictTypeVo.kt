package com.blank.system.domain.vo

import com.blank.system.domain.SysDictType
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 字典类型视图对象 sys_dict_type
 */
@AutoMapper(target = SysDictType::class)
class SysDictTypeVo : Serializable {
    /**
     * 字典主键
     */
    var dictId: Long? = null

    /**
     * 字典名称
     */
    var dictName: String? = null

    /**
     * 字典类型
     */
    var dictType: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 创建时间
     */
    var createTime: Date? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
