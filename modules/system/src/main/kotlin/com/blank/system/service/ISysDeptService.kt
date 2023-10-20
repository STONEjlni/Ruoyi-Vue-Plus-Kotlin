package com.blank.system.service

import cn.hutool.core.lang.tree.Tree
import com.blank.system.domain.bo.SysDeptBo
import com.blank.system.domain.vo.SysDeptVo

/**
 * 部门管理 服务层
 */
interface ISysDeptService {
    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    fun selectDeptList(dept: SysDeptBo): MutableList<SysDeptVo>

    /**
     * 查询部门树结构信息
     *
     * @param dept 部门信息
     * @return 部门树信息集合
     */
    fun selectDeptTreeList(dept: SysDeptBo): MutableList<Tree<Long>>

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    fun buildDeptTreeSelect(depts: MutableList<SysDeptVo>): MutableList<Tree<Long>>

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    fun selectDeptListByRoleId(roleId: Long): MutableList<Long>

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    fun selectDeptById(deptId: Long): SysDeptVo?

    /**
     * 根据ID查询所有子部门数（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    fun selectNormalChildrenDeptById(deptId: Long): Long

    /**
     * 是否存在部门子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    fun hasChildByDeptId(deptId: Long): Boolean

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    fun checkDeptExistUser(deptId: Long): Boolean

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    fun checkDeptNameUnique(dept: SysDeptBo): Boolean

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    fun checkDeptDataScope(deptId: Long)

    /**
     * 新增保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    fun insertDept(bo: SysDeptBo): Int

    /**
     * 修改保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    fun updateDept(bo: SysDeptBo): Int

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    fun deleteDeptById(deptId: Long): Int
}
