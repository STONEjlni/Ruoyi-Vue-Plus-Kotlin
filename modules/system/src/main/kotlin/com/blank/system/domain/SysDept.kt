package com.blank.system.domain

import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import java.io.Serial

/**
 * 部门表 sys_dept
 */
@Table("sys_dept")
class SysDept : BaseEntity() {
    /**
     * 部门ID
     */
    @Id
    var deptId: Long? = null

    /**
     * 父部门ID
     */
    var parentId: Long? = null

    /**
     * 部门名称
     */
    var deptName: String? = null

    /**
     * 显示顺序
     */
    var orderNum: Int? = null

    /**
     * 负责人
     */
    var leader: Long? = null

    /**
     * 联系电话
     */
    var phone: String? = null

    /**
     * 邮箱
     */
    var email: String? = null

    /**
     * 部门状态:0正常,1停用
     */
    var status: String? = null

    /**
     * 祖级列表
     */
    var ancestors: String? = null

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
