package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.core.constant.UserConstants
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 字典数据表 sys_dict_data
 */
@Table("sys_dict_data")
@Open
@NoArg
class SysDictData : BaseEntity() {
    /**
     * 字典编码
     */
    @Id
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
     * 表格字典样式
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

    fun getDefault(): Boolean {
        return UserConstants.YES == this.isDefault
    }
}
