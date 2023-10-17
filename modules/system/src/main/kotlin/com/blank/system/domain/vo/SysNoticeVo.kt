package com.blank.system.domain.vo

import com.blank.common.translation.annotation.Translation
import com.blank.common.translation.constant.TransConstant
import com.blank.system.domain.SysNotice
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 通知公告视图对象 sys_notice
 */
@AutoMapper(target = SysNotice::class)
class SysNoticeVo : Serializable {
    /**
     * 公告ID
     */
    var noticeId: Long? = null

    /**
     * 公告标题
     */
    var noticeTitle: String? = null

    /**
     * 公告类型（1通知 2公告）
     */
    var noticeType: String? = null

    /**
     * 公告内容
     */
    var noticeContent: String? = null

    /**
     * 公告状态（0正常 1关闭）
     */
    var status: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 创建者
     */
    var createBy: Long? = null

    /**
     * 创建人名称
     */
    @Translation(type = TransConstant.USER_ID_TO_NAME, mapper = "createBy")
    var createByName: String? = null

    /**
     * 创建时间
     */
    var createTime: Date? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
