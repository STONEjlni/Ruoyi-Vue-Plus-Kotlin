package com.blank.system.domain.vo

import com.blank.system.domain.SysDept
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 部门视图对象 sys_dept
 */
@AutoMapper(target = SysDept::class)
class SysDeptVo : Serializable {
    /**
     * 部门id
     */
    var deptId: Long? = null

    /**
     * 父部门id
     */
    var parentId: Long? = null

    /**
     * 父部门名称
     */
    var parentName: String? = null

    /**
     * 祖级列表
     */
    var ancestors: String? = null

    /**
     * 部门名称
     */
    var deptName: String? = null

    /**
     * 显示顺序
     */
    var orderNum: Int? = null

    /**
     * 负责人ID
     */
    var leader: Long? = null

    /**
     * 负责人
     */
    var leaderName: String? = null

    /**
     * 联系电话
     */
    var phone: String? = null

    /**
     * 邮箱
     */
    var email: String? = null

    /**
     * 部门状态（0正常 1停用）
     */
    var status: String? = null

    /**
     * 创建时间
     */
    var createTime: Date? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
