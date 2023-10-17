package com.blank.system.domain.vo

import com.blank.common.translation.annotation.Translation
import com.blank.common.translation.constant.TransConstant
import com.blank.system.domain.SysOss
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * OSS对象存储视图对象 sys_oss
 */
@AutoMapper(target = SysOss::class)
class SysOssVo : Serializable {
    /**
     * 对象存储主键
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
     * 创建时间
     */
    var createTime: Date? = null

    /**
     * 上传人
     */
    var createBy: Long? = null

    /**
     * 上传人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createBy")
    var createByName: String? = null

    /**
     * 服务商
     */
    var service: String? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
