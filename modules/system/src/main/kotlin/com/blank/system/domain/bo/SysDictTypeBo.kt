package com.blank.system.domain.bo

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysDictType
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

/**
 * 字典类型业务对象 sys_dict_type
 */
@AutoMapper(target = SysDictType::class, reverseConvertGenerate = false)
class SysDictTypeBo : BaseEntity() {
    /**
     * 字典主键
     */
    var dictId: Long? = null

    /**
     * 字典名称
     */
    var dictName: @NotBlank(message = "字典名称不能为空") @Size(
        min = 0,
        max = 100,
        message = "字典类型名称长度不能超过{max}个字符"
    ) String? = null

    /**
     * 字典类型
     */
    var dictType: @NotBlank(message = "字典类型不能为空") @Size(
        min = 0,
        max = 100,
        message = "字典类型类型长度不能超过{max}个字符"
    ) @Pattern(
        regexp = "^[a-z][a-z0-9_]*$",
        message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）"
    ) String? = null

    /**
     * 备注
     */
    var remark: String? = null
}
