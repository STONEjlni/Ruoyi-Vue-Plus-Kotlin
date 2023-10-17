package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.lang.tree.Tree
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.DeptService
import com.blank.common.core.utils.SpringUtilExtend.getAopProxy
import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.StringUtilsExtend.splitTo
import com.blank.common.core.utils.TreeBuildUtils.build
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.system.domain.SysDept
import com.blank.system.domain.bo.SysDeptBo
import com.blank.system.domain.vo.SysDeptVo
import com.blank.system.mapper.SysDeptMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysUserMapper
import com.blank.system.service.ISysDeptService
import com.mybatisflex.core.query.QueryWrapper
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
    override fun selectDeptList(dept: SysDeptBo): MutableList<SysDeptVo>? {
        /*LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(dept);
        return baseMapper.selectDeptList(lqw);*/
        return null
    }

    /**
     * 查询部门树结构信息
     *
     * @param bo 部门信息
     * @return 部门树信息集合
     */
    override fun selectDeptTreeList(bo: SysDeptBo): MutableList<Tree<Long>>? {
        // 只查询未禁用部门
        /*bo.setStatus(UserConstants.DEPT_NORMAL);
        LambdaQueryWrapper<SysDept> lqw = buildQueryWrapper(bo);
        List<SysDeptVo> depts = baseMapper.selectDeptList(lqw);
        return buildDeptTreeSelect(depts);*/
        return null
    }

    private fun  /*<SysDept>*/buildQueryWrapper(bo: SysDeptBo): QueryWrapper? {
        /*LambdaQueryWrapper<SysDept> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysDept::getDelFlag, "0");
        lqw.eq(ObjectUtil.isNotNull(bo.getDeptId()), SysDept::getDeptId, bo.getDeptId());
        lqw.eq(ObjectUtil.isNotNull(bo.getParentId()), SysDept::getParentId, bo.getParentId());
        lqw.like(StrUtil.isNotBlank(bo.getDeptName()), SysDept::getDeptName, bo.getDeptName());
        lqw.eq(StrUtil.isNotBlank(bo.getStatus()), SysDept::getStatus, bo.getStatus());
        lqw.orderByAsc(SysDept::getDeptId);
        lqw.orderByAsc(SysDept::getParentId);
        lqw.orderByAsc(SysDept::getOrderNum);
        return lqw;*/
        return null
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param depts 部门列表
     * @return 下拉树结构列表
     */
    override fun buildDeptTreeSelect(depts: MutableList<SysDeptVo>): MutableList<Tree<Long>>? {
        return if (CollUtil.isEmpty(depts)) {
            CollUtil.newArrayList()
        } else build(depts) { dept: SysDeptVo, tree: Tree<Long> ->
            tree.setId(dept.deptId)
                .setParentId(dept.parentId)
                .setName(dept.deptName)
                .setWeight(dept.orderNum)
        }?.toMutableList()
    }

    /**
     * 根据角色ID查询部门树信息
     *
     * @param roleId 角色ID
     * @return 选中部门列表
     */
    override fun selectDeptListByRoleId(roleId: Long): MutableList<Long>? {
        /*SysRole role = roleMapper.selectById(roleId);
        return baseMapper.selectDeptListByRoleId(roleId, role.getDeptCheckStrictly());*/
        return null
    }

    /**
     * 根据部门ID查询信息
     *
     * @param deptId 部门ID
     * @return 部门信息
     */
    @Cacheable(cacheNames = [CacheNames.SYS_DEPT], key = "#deptId")
    override fun selectDeptById(deptId: Long): SysDeptVo? {
        /*SysDeptVo dept = baseMapper.selectVoById(deptId);
        if (ObjectUtil.isNull(dept)) {
            return null;
        }
        SysDeptVo parentDept = baseMapper.selectVoOne(new LambdaQueryWrapper<SysDept>()
            .select(SysDept::getDeptName).eq(SysDept::getDeptId, dept.getParentId()));
        dept.setParentName(ObjectUtil.isNotNull(parentDept) ? parentDept.getDeptName() : null);
        return dept;*/
        return null
    }

    /**
     * 通过部门ID查询部门名称
     *
     * @param deptIds 部门ID串逗号分隔
     * @return 部门名称串逗号分隔
     */
    override fun selectDeptNameByIds(deptIds: String): String? {
        val list: MutableList<String> = ArrayList()
        for (id in splitTo<Long>(deptIds) { value: Any? -> Convert.toLong(value) }) {
            val vo = getAopProxy(this).selectDeptById(id)
            if (ObjectUtil.isNotNull(vo)) {
                list.add(vo!!.deptName!!)
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
        /*return baseMapper.selectCount(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getStatus, UserConstants.DEPT_NORMAL)
            .apply(DataBaseHelper.findInSet(deptId, "ancestors")));*/
        return 0
    }

    /**
     * 是否存在子节点
     *
     * @param deptId 部门ID
     * @return 结果
     */
    override fun hasChildByDeptId(deptId: Long): Boolean {
        /*return baseMapper.exists(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getParentId, deptId));*/
        return false
    }

    /**
     * 查询部门是否存在用户
     *
     * @param deptId 部门ID
     * @return 结果 true 存在 false 不存在
     */
    override fun checkDeptExistUser(deptId: Long): Boolean {
        /*return userMapper.exists(new LambdaQueryWrapper<SysUser>()
            .eq(SysUser::getDeptId, deptId));*/
        return false
    }

    /**
     * 校验部门名称是否唯一
     *
     * @param dept 部门信息
     * @return 结果
     */
    override fun checkDeptNameUnique(dept: SysDeptBo): Boolean {
        /*boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDept>()
            .eq(SysDept::getDeptName, dept.getDeptName())
            .eq(SysDept::getParentId, dept.getParentId())
            .ne(ObjectUtil.isNotNull(dept.getDeptId()), SysDept::getDeptId, dept.getDeptId()));
        return !exist;*/
        return false
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
        /*SysDept info = baseMapper.selectById(bo.getParentId());
        // 如果父节点不为正常状态,则不允许新增子节点
        if (!UserConstants.DEPT_NORMAL.equals(info.getStatus())) {
            throw new ServiceException("部门停用，不允许新增");
        }
        SysDept dept = MapstructUtils.convert(bo, SysDept.class);
        dept.setAncestors(info.getAncestors() + StringUtilsExtend.SEPARATOR + dept.getParentId());
        return baseMapper.insert(dept);*/
        return 0
    }

    /**
     * 修改保存部门信息
     *
     * @param bo 部门信息
     * @return 结果
     */
    @CacheEvict(cacheNames = [CacheNames.SYS_DEPT], key = "#bo.deptId")
    override fun updateDept(bo: SysDeptBo): Int {
        /*SysDept dept = MapstructUtils.convert(bo, SysDept.class);
        SysDept oldDept = baseMapper.selectById(dept.getDeptId());
        if (!oldDept.getParentId().equals(dept.getParentId())) {
            // 如果是新父部门 则校验是否具有新父部门权限 避免越权
            this.checkDeptDataScope(dept.getParentId());
            SysDept newParentDept = baseMapper.selectById(dept.getParentId());
            if (ObjectUtil.isNotNull(newParentDept) && ObjectUtil.isNotNull(oldDept)) {
                String newAncestors = newParentDept.getAncestors() + StringUtilsExtend.SEPARATOR + newParentDept.getDeptId();
                String oldAncestors = oldDept.getAncestors();
                dept.setAncestors(newAncestors);
                updateDeptChildren(dept.getDeptId(), newAncestors, oldAncestors);
            }
        }
        int result = baseMapper.updateById(dept);
        if (UserConstants.DEPT_NORMAL.equals(dept.getStatus()) && StringUtils.isNotEmpty(dept.getAncestors())
            && !StringUtils.equals(UserConstants.DEPT_NORMAL, dept.getAncestors())) {
            // 如果该部门是启用状态，则启用该部门的所有上级部门
            updateParentDeptStatusNormal(dept);
        }
        return result;*/
        return 0
    }

    /**
     * 修改该部门的父级部门状态
     *
     * @param dept 当前部门
     */
    private fun updateParentDeptStatusNormal(dept: SysDept) {
        /*String ancestors = dept.getAncestors();
        Long[] deptIds = Convert.toLongArray(ancestors);
        baseMapper.update(null, new LambdaUpdateWrapper<SysDept>()
            .set(SysDept::getStatus, UserConstants.DEPT_NORMAL)
            .in(SysDept::getDeptId, Arrays.asList(deptIds)));*/
    }

    /**
     * 修改子元素关系
     *
     * @param deptId       被修改的部门ID
     * @param newAncestors 新的父ID集合
     * @param oldAncestors 旧的父ID集合
     */
    private fun updateDeptChildren(deptId: Long, newAncestors: String, oldAncestors: String) {
        /*List<SysDept> children = baseMapper.selectList(new LambdaQueryWrapper<SysDept>()
            .apply(DataBaseHelper.findInSet(deptId, "ancestors")));
        List<SysDept> list = new ArrayList<>();
        for (SysDept child : children) {
            SysDept dept = new SysDept();
            dept.setDeptId(child.getDeptId());
            dept.setAncestors(child.getAncestors().replaceFirst(oldAncestors, newAncestors));
            list.add(dept);
        }
        if (CollUtil.isNotEmpty(list)) {
            if (baseMapper.updateBatchById(list)) {
                list.forEach(dept -> CacheUtils.evict(CacheNames.SYS_DEPT, dept.getDeptId()));
            }
        }*/
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
