package com.blank.system.domain.bo

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysDept
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 部门业务对象 sys_dept
 */
@AutoMapper(target = SysDept::class, reverseConvertGenerate = false)
class SysDeptBo : BaseEntity() {
    /**
     * 部门id
     */
    var deptId: @NotNull(message = "部门id不能为空") Long? = null

    /**
     * 父部门ID
     */
    var parentId: Long? = null

    /**
     * 部门名称
     */
    var deptName: @NotBlank(message = "部门名称不能为空") @Size(
        min = 0,
        max = 30,
        message = "部门名称长度不能超过{max}个字符"
    ) String? = null

    /**
     * 显示顺序
     */
    var orderNum: @NotNull(message = "显示顺序不能为空") Int? = null

    /**
     * 负责人
     */
    var leader: Long? = null

    /**
     * 联系电话
     */
    var phone: @Size(min = 0, max = 11, message = "联系电话长度不能超过{max}个字符") String? = null

    /**
     * 邮箱
     */
    var email: @Email(message = "邮箱格式不正确") @Size(
        min = 0,
        max = 50,
        message = "邮箱长度不能超过{max}个字符"
    ) String? = null

    /**
     * 部门状态（0正常 1停用）
     */
    var status: String? = null
}
