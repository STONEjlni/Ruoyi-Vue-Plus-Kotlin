package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.lang.tree.Tree
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.DeptService
import com.blank.common.core.utils.MapstructUtils
import com.blank.common.core.utils.SpringUtilExtend.getAopProxy
import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.StringUtilsExtend.splitTo
import com.blank.common.core.utils.TreeBuildUtils.build
import com.blank.common.mybatis.helper.DataBaseHelper
import com.blank.common.redis.utils.CacheUtils
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.system.domain.SysDept
import com.blank.system.domain.SysRole
import com.blank.system.domain.bo.SysDeptBo
import com.blank.system.domain.table.SysDeptDef.SYS_DEPT
import com.blank.system.domain.vo.SysDeptVo
import com.blank.system.mapper.SysDeptMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysUserMapper
import com.blank.system.service.ISysDeptService
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.row.Db
import com.mybatisflex.core.update.UpdateWrapper
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 部门管理 服务实现
 */
@Service
class SysDeptServiceImpl(
    private val baseMapper: SysDeptMapper,
    private val roleMapper: SysRoleMapper,
    private val userMapper: SysUserMapper
) : ISysDeptService, DeptService {


    /**
     * 查询部门管理数据
     *
     * @param dept 部门信息
     * @return 部门信息集合
     */
    override fun selectDeptList(dept: SysDeptBo): MutableList<SysDeptVo> {
        return baseMapper.selectDeptList(buildQueryWrapper(dept))
    }

    /**
     * 查询部门树结构信息
     *
     * @param bo 部门信息
     * @return 部门树信息集合
     */
    override fun selectDeptTreeList(bo: SysDeptBo): MutableList<Tree<Long>> {
        // 只查询未禁用部门
        bo.status = UserConstants.DEPT_NORMAL
        val depts: MutableList<SysDeptVo> = baseMapper.selectDeptList(buildQueryWrapper(bo))
        return buildDeptTreeSelect(depts)
    }

    private fun buildQueryWrapper(bo: SysDeptBo): QueryWrapper {
        return QueryWrapper.create()
            .select()
            .from(SYS_DEPT)
            .where(SYS_DEPT.DEPT_ID.eq(bo.deptId))
            .and(SYS_DEPT.PARENT_ID.eq(bo.parentId))
            .and(SYS_DEPT.DEPT_NAME.like(bo.deptName))
            .and(SYS_DEPT.STATUS.eq(bo.status))
            .orderBy(SYS_DEPT.PARENT_ID, true)
            .orderBy(SYS_DEPT.ORDER_NUM, true)
            .orderBy(SYS_DEPT.DEPT_ID, true)
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    override fun buildDeptTreeSelect(depts: MutableList<SysDeptVo>): MutableList<Tree<Long>> {
        return if (CollUtil.isEmpty(depts)) {
            CollUtil.newArrayList()
        } else build(depts) { dept, tree: Tree<Long> ->
            tree.setId(dept.deptId)
                .setParentId(dept.parentId)
                .setName(dept.deptName)
                .setWeight(dept.orderNum)
        }
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    override fun selectDeptListByRoleId(roleId: Long): MutableList<Long> {
        val role: SysRole = roleMapper.selectOneById(roleId)
        return baseMapper.selectDeptListByRoleId(roleId, role.deptCheckStrictly!!)
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Cacheable(cacheNames = [CacheNames.SYS_DEPT], key = "#deptId")
    override fun selectDeptById(deptId: Long): SysDeptVo? {
        val dept = baseMapper.selectOneWithRelationsByIdAs(deptId, SysDeptVo::class.java)
        if (ObjectUtil.isNull(dept)) {
            return null
        }
        val parentDept = baseMapper.selectOneByQueryAs(
            QueryWrapper.create()
                .from(SYS_DEPT)
                .select(SYS_DEPT.DEPT_NAME)
                .where(SYS_DEPT.DEPT_ID.eq(dept.parentId)),
            SysDeptVo::class.java
        )
        dept.parentName = if (ObjectUtil.isNotNull(parentDept)) parentDept.deptName else null
        return dept
    }

    /**
     * 通过部门ID查询部门名称
     *
     * @param deptIds 部门ID串逗号分隔
     * @return 部门名称串逗号分隔
     */
    override fun selectDeptNameByIds(deptIds: String): String {
        val list: MutableList<String> = ArrayList()
        for (id in splitTo(deptIds, Convert::toLong)) {
            val vo: SysDeptVo = getAopProxy(this).selectDeptById(id)!!
            if (ObjectUtil.isNotNull(vo)) {
                list.add(vo.deptName!!)
            }
        }
        return java.lang.String.join(StringUtilsExtend.SEPARATOR, list)
    }

    /**
     * 根据ID查询所有子部门数（正常状态）
     *
     * @param deptId 部门ID
     * @return 子部门数
     */
    override fun selectNormalChildrenDeptById(deptId: Long): Long {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create()
                .from(SYS_DEPT)
                .where(
                    SYS_DEPT.STATUS.eq(UserConstants.DEPT_NORMAL)
                        .and(DataBaseHelper.findInSet(deptId, "ancestors"))
                )
        )
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    override fun hasChildByDeptId(deptId: Long): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_DEPT).where(SYS_DEPT.PARENT_ID.eq(deptId))
        ) > 0
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    override fun checkDeptExistUser(deptId: Long): Boolean {
        return userMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_DEPT).where(SYS_DEPT.DEPT_ID.eq(deptId))
        ) > 0
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    override fun checkDeptNameUnique(dept: SysDeptBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create()
                .from(SYS_DEPT)
                .where(
                    SYS_DEPT.DEPT_NAME.eq(dept.deptName)
                        .and(
                            SYS_DEPT.PARENT_ID.eq(dept.parentId)
                                .and(SYS_DEPT.DEPT_ID.ne(dept.deptId))
                        )
                )
        ) <= 0
    }

    /**
     * 校验部门是否有数据权限
     *
     * @param deptId 部门id
     */
    override fun checkDeptDataScope(deptId: Long) {
        if (ObjectUtil.isNull(deptId)) {
            return
        }
        if (isSuperAdmin()) {
            return
        }
        val dept = baseMapper.selectDeptById(deptId)
        if (ObjectUtil.isNull(dept)) {
            throw ServiceException("没有权限访问部门数据！")
        }
    }

    /**
     * 新增保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    override fun insertDept(bo: SysDeptBo): Int {
        val info = baseMapper.selectOneById(bo.parentId)
        // 如果父节点不为正常状态,则不允许新增子节点
        if (UserConstants.DEPT_NORMAL != info.status) {
            throw ServiceException("部门停用，不允许新增")
        }
        val dept: SysDept = MapstructUtils.convert(bo, SysDept::class.java)!!
        dept.ancestors = info.ancestors + StringUtilsExtend.SEPARATOR + dept.parentId
        return baseMapper.insert(dept, true)
    }

    /**
     * 修改保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    @CacheEvict(cacheNames = [CacheNames.SYS_DEPT], key = "#bo.deptId")
    override fun updateDept(bo: SysDeptBo): Int {
        val dept: SysDept = MapstructUtils.convert(bo, SysDept::class.java)!!
        val oldDept = baseMapper.selectOneById(dept.deptId)
        if (oldDept.parentId != dept.parentId) {
            // 如果是新父部门 则校验是否具有新父部门权限 避免越权
            this.checkDeptDataScope(dept.parentId!!)
            val newParentDept = baseMapper.selectOneById(dept.parentId)
            if (ObjectUtil.isNotNull(newParentDept) && ObjectUtil.isNotNull(oldDept)) {
                val newAncestors: String =
                    newParentDept.ancestors + StringUtilsExtend.SEPARATOR + newParentDept.deptId
                val oldAncestors: String = oldDept.ancestors!!
                dept.ancestors = newAncestors
                updateDeptChildren(dept.deptId!!, newAncestors, oldAncestors)
            }
        }
        val result = baseMapper.update(dept)
        if (UserConstants.DEPT_NORMAL == dept.status && StrUtil.isNotEmpty(dept.ancestors)
            && !StrUtil.equals(UserConstants.DEPT_NORMAL, dept.ancestors)
        ) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept)
        }
        return result
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private fun updateParentDeptStatusNormal(dept: SysDept) {
        val ancestors: String? = dept.ancestors
        val deptIds = Convert.toLongArray(ancestors)
        val sysDept = UpdateWrapper.of(SysDept::class.java)
            .set(SysDept::status, UserConstants.DEPT_NORMAL).toEntity()
        baseMapper.updateByQuery(
            sysDept,
            QueryWrapper.create().from(SYS_DEPT).where(SYS_DEPT.DEPT_ID.`in`(listOf(*deptIds)))
        )
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    private fun updateDeptChildren(deptId: Long, newAncestors: String, oldAncestors: String) {
        val children = baseMapper.selectListByQuery(
            QueryWrapper.create().from(SYS_DEPT)
                .where(DataBaseHelper.findInSet(deptId, "ancestors"))
        )
        val list: MutableList<SysDept> = ArrayList()
        for (child in children) {
            val dept = SysDept()
            dept.deptId = child.deptId
            dept.ancestors = child.ancestors?.replaceFirst(oldAncestors, newAncestors)
            list.add(dept)
        }
        if (CollUtil.isNotEmpty(list)) {
            if (Db.updateEntitiesBatch(list) > 0) {
                list.forEach{ dept: SysDept ->
                    CacheUtils.evict(
                        CacheNames.SYS_DEPT,
                        dept.deptId
                    )
                }
            }
        }
    }

    /**
     * 删除部门管理信息
     *
     * @param deptId 部门ID
     * @return 结果
     */
    @CacheEvict(cacheNames = [CacheNames.SYS_DEPT], key = "#deptId")
    override fun deleteDeptById(deptId: Long): Int {
        return baseMapper.deleteById(deptId)
    }
}
