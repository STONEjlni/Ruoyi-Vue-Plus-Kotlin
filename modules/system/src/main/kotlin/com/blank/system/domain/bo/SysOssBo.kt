package com.blank.system.domain.bo

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysOss
import io.github.linpeilie.annotations.AutoMapper

/**
 * OSS对象存储分页查询对象 sys_oss
 */
@AutoMapper(target = SysOss::class, reverseConvertGenerate = false)
class SysOssBo : BaseEntity() {
    /**
     * ossId
     */
    var ossId: Long? = null

    /**
     * 文件名
     */
    var fileName: String? = null

    /**
     * 原名
     */
    var originalName: String? = null

    /**
     * 文件后缀名
     */
    var fileSuffix: String? = null

    /**
     * URL地址
     */
    var url: String? = null

    /**
     * 服务商
     */
    var service: String? = null
}
