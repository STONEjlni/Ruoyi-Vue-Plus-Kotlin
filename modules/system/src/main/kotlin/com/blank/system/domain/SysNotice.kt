package com.blank.system.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table

/**
 * 通知公告表 sys_notice
 */
@Table("sys_notice")
@Open
@NoArg
class SysNotice : BaseEntity() {
    /**
     * 公告ID
     */
    @Id
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
}
