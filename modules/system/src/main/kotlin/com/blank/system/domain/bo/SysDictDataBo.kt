package com.blank.system.domain.bo

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysDictData
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 字典数据业务对象 sys_dict_data
 */
@AutoMapper(target = SysDictData::class, reverseConvertGenerate = false)
class SysDictDataBo : BaseEntity() {
    /**
     * 字典编码
     */
    var dictCode: @NotNull(message = "字典编码不能为空") Long? = null

    /**
     * 字典排序
     */
    var dictSort: Int? = null

    /**
     * 字典标签
     */
    var dictLabel: @NotBlank(message = "字典标签不能为空") @Size(
        min = 0,
        max = 100,
        message = "字典标签长度不能超过{max}个字符"
    ) String? = null

    /**
     * 字典键值
     */
    var dictValue: @NotBlank(message = "字典键值不能为空") @Size(
        min = 0,
        max = 100,
        message = "字典键值长度不能超过{max}个字符"
    ) String? = null

    /**
     * 字典类型
     */
    var dictType: @NotBlank(message = "字典类型不能为空") @Size(
        min = 0,
        max = 100,
        message = "字典类型长度不能超过{max}个字符"
    ) String? = null

    /**
     * 样式属性（其他样式扩展）
     */
    var cssClass: @Size(min = 0, max = 100, message = "样式属性长度不能超过{max}个字符") String? = null

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
}
