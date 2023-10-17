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
    private val ossId: Long? = null

    /**
     * 文件名
     */
    private val fileName: String? = null

    /**
     * 原名
     */
    private val originalName: String? = null

    /**
     * 文件后缀名
     */
    private val fileSuffix: String? = null

    /**
     * URL地址
     */
    private val url: String? = null

    /**
     * 服务商
     */
    private val service: String? = null
}
