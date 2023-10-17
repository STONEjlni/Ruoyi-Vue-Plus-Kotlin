package com.blank.system.domain.bo

import com.blank.common.core.validate.AddGroup
import com.blank.common.core.validate.EditGroup
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysConfig
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 参数配置业务对象 sys_config
 */
@AutoMapper(target = SysConfig::class, reverseConvertGenerate = false)
class SysConfigBo : BaseEntity() {
    /**
     * 参数主键
     */
    var configId: @NotNull(message = "参数主键不能为空", groups = [EditGroup::class]) Long? = null

    /**
     * 参数名称
     */
    var configName: @NotBlank(message = "参数名称不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 0,
        max = 100,
        message = "参数名称不能超过{max}个字符"
    ) String? = null

    /**
     * 参数键名
     */
    var configKey: @NotBlank(message = "参数键名不能为空", groups = [AddGroup::class, EditGroup::class]) @Size(
        min = 0,
        max = 100,
        message = "参数键名长度不能超过{max}个字符"
    ) String? = null

    /**
     * 参数键值
     */
    var configValue: @NotBlank(
        message = "参数键值不能为空",
        groups = [AddGroup::class, EditGroup::class]
    ) @Size(min = 0, max = 500, message = "参数键值长度不能超过{max}个字符") String? = null

    /**
     * 系统内置（Y是 N否）
     */
    var configType: String? = null

    /**
     * 备注
     */
    var remark: String? = null
}
