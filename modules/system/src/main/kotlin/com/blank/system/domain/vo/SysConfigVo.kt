package com.blank.system.domain.vo

import com.blank.system.domain.SysConfig
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 参数配置视图对象 sys_config
 */
@AutoMapper(target = SysConfig::class)
class SysConfigVo : Serializable {
    /**
     * 参数主键
     */
    var configId: Long? = null

    /**
     * 参数名称
     */
    var configName: String? = null

    /**
     * 参数键名
     */
    var configKey: String? = null

    /**
     * 参数键值
     */
    var configValue: String? = null

    /**
     * 系统内置（Y是 N否）
     */
    var configType: String? = null

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
