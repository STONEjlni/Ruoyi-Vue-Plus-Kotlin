package com.blank.system.domain

import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 参数配置表 sys_config
 */
@Table("sys_config")
class SysConfig : BaseEntity() {
    /**
     * 参数主键
     */
    @Id
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
}
