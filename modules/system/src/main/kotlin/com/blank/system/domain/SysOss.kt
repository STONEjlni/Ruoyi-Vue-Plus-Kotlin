package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * OSS对象存储对象
 *
 */
@Table("sys_oss")
@Open
@NoArg
class SysOss : BaseEntity() {
    /**
     * 对象存储主键
     */
    @Id
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
