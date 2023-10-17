package com.blank.system.domain.vo

import com.blank.system.domain.SysDictData
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 字典数据视图对象 sys_dict_data
 */
@AutoMapper(target = SysDictData::class)
class SysDictDataVo : Serializable {
    /**
     * 字典编码
     */
    var dictCode: Long? = null

    /**
     * 字典排序
     */
    var dictSort: Int? = null

    /**
     * 字典标签
     */
    var dictLabel: String? = null

    /**
     * 字典键值
     */
    var dictValue: String? = null

    /**
     * 字典类型
     */
    var dictType: String? = null

    /**
     * 样式属性（其他样式扩展）
     */
    var cssClass: String? = null

    /**
     * 表格回显样式
     */
    var listClass: String? = null

    /**
     * 是否默认（Y是 N否）
     */
    var isDefault: String? = null

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
