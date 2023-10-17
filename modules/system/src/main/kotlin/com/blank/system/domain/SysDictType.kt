package com.blank.system.domain

import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 字典类型表 sys_dict_type
 */
@Table("sys_dict_type")
class SysDictType : BaseEntity() {
    /**
     * 字典主键
     */
    @Id
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
}
