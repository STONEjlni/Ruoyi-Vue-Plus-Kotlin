package com.blank.common.core.service

/**
 * 通用 部门服务
 */
interface DeptService {
    /**
     * 通过部门ID查询部门名称
     *
     * @param deptIds 部门ID串逗号分隔
     * @return 部门名称串逗号分隔
     */
    fun selectDeptNameByIds(deptIds: String): String?
}
