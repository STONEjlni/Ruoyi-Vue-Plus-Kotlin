package com.blank.system.service.impl

import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.domain.model.LoginUser
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.MapstructUtils
import com.blank.common.core.utils.StreamUtils
import com.blank.common.core.utils.StringUtilsExtend.splitList
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.satoken.utils.LoginHelper
import com.blank.system.domain.SysRole
import com.blank.system.domain.SysRoleDept
import com.blank.system.domain.SysRoleMenu
import com.blank.system.domain.SysUserRole
import com.blank.system.domain.bo.SysRoleBo
import com.blank.system.domain.table.SysRoleDef.SYS_ROLE
import com.blank.system.domain.table.SysRoleDeptDef.SYS_ROLE_DEPT
import com.blank.system.domain.table.SysRoleMenuDef.SYS_ROLE_MENU
import com.blank.system.domain.table.SysUserRoleDef.SYS_USER_ROLE
import com.blank.system.domain.vo.SysRoleVo
import com.blank.system.mapper.SysRoleDeptMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysRoleMenuMapper
import com.blank.system.mapper.SysUserRoleMapper
import com.blank.system.service.ISysRoleService
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.row.Db
import com.mybatisflex.core.update.UpdateChain
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*


/**
 * 角色 业务层处理
 */
@Service
class SysRoleServiceImpl(
    private val baseMapper: SysRoleMapper,
    private val roleMenuMapper: SysRoleMenuMapper,
    private val userRoleMapper: SysUserRoleMapper,
    private val roleDeptMapper: SysRoleDeptMapper
) : ISysRoleService {

    override fun selectPageRoleList(role: SysRoleBo, pageQuery: PageQuery): TableDataInfo<SysRoleVo> {
        val page = baseMapper.selectPageRoleList(pageQuery, buildQueryWrapper(role))
        return TableDataInfo.build(page)
    }

    /**
     * 根据条件分页查询角色数据
     *
     * @param role 角色信息
     * @return 角色数据集合信息
     */
    override fun selectRoleList(role: SysRoleBo): MutableList<SysRoleVo> {
        return baseMapper.selectRoleList(buildQueryWrapper(role))
    }

    private fun buildQueryWrapper(bo: SysRoleBo): QueryWrapper {
        val params: Map<String, Any> = bo.params
        return QueryWrapper.create()
            .where(SYS_ROLE.ROLE_ID.eq(bo.roleId))
            .and(SYS_ROLE.ROLE_NAME.like(bo.roleName))
            .and(SYS_ROLE.STATUS.eq(bo.status))
            .and(SYS_ROLE.ROLE_KEY.like(bo.roleKey))
            .and(
                SYS_ROLE.CREATE_TIME.between(
                    params["beginTime"],
                    params["endTime"],
                    params["beginTime"] != null && params["endTime"] != null
                )
            )
            .orderBy(SYS_ROLE.ROLE_SORT, true)
            .orderBy(SYS_ROLE.CREATE_TIME, true)
    }

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    override fun selectRolesByUserId(userId: Long): MutableList<SysRoleVo> {
        val userRoles = baseMapper.selectRolePermissionByUserId(userId)
        val roles = selectRoleAll()
        for (role in roles) {
            for (userRole in userRoles) {
                if (role.roleId == userRole.roleId) {
                    role.flag = true
                    break
                }
            }
        }
        return roles
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    override fun selectRolePermissionByUserId(userId: Long): MutableSet<String> {
        val perms = baseMapper.selectRolePermissionByUserId(userId)
        val permsSet: MutableSet<String> = HashSet()
        for (perm in perms) {
            if (ObjectUtil.isNotNull(perm)) {
                permsSet.addAll(splitList(perm.roleKey!!.trim()))
            }
        }
        return permsSet
    }

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    override fun selectRoleAll(): MutableList<SysRoleVo> {
        return selectRoleList(SysRoleBo())
    }

    /**
     * 根据用户ID获取角色选择框列表
     *
     * @param userId 用户ID
     * @return 选中角色ID列表
     */
    override fun selectRoleListByUserId(userId: Long): MutableList<Long> {
        return baseMapper.selectRoleListByUserId(userId)
    }

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    override fun selectRoleById(roleId: Long): SysRoleVo {
        return baseMapper.selectRoleById(roleId)
    }

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    override fun checkRoleNameUnique(role: SysRoleBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_ROLE)
                .where(SYS_ROLE.ROLE_NAME.eq(role.roleName))
                .and(SYS_ROLE.ROLE_ID.ne(role.roleId))
        ) == 0.toLong()
    }

    /**
     * 校验角色权限是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    override fun checkRoleKeyUnique(role: SysRoleBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_ROLE)
                .where(SYS_ROLE.ROLE_KEY.eq(role.roleKey))
                .and(SYS_ROLE.ROLE_ID.ne(role.roleId))
        ) == 0.toLong()
    }

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    override fun checkRoleAllowed(role: SysRoleBo) {
        if (ObjectUtil.isNotNull(role.roleId) && LoginHelper.isSuperAdmin(role.roleId!!)) {
            throw ServiceException("不允许操作超级管理员角色")
        }
        val keys = arrayOf(UserConstants.SUPER_ADMIN_ROLE_KEY)
        // 新增不允许使用 管理员标识符
        if (ObjectUtil.isNull(role.roleId)
            && StrUtil.equalsAny(role.roleKey, *keys)) {
            throw ServiceException("不允许使用系统内置管理员角色标识符!")
        }
        // 修改不允许修改 管理员标识符
        if (ObjectUtil.isNotNull(role.roleId)) {
            val sysRole = baseMapper.selectOneById(role.roleId)
            // 如果标识符不相等 判断为修改了管理员标识符
            if (!StrUtil.equals(sysRole.roleKey, role.roleKey)) {
                if (StrUtil.equalsAny(sysRole.roleKey, *keys)) {
                    throw ServiceException("不允许修改系统内置管理员角色标识符!")
                } else if (StrUtil.equalsAny(role.roleKey, *keys)) {
                    throw ServiceException("不允许使用系统内置管理员角色标识符!")
                }
            }
        }
    }

    /**
     * 校验角色是否有数据权限
     *
     * @param roleId 角色id
     */
    override fun checkRoleDataScope(roleId: Long) {
        if (ObjectUtil.isNull(roleId)) {
            return
        }
        if (LoginHelper.isSuperAdmin()) {
            return
        }
        val roles: MutableList<SysRoleVo> = selectRoleList(SysRoleBo(roleId))
        if (CollUtil.isEmpty(roles)) {
            throw ServiceException("没有权限访问角色数据！")
        }
    }

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    override fun countUserRoleByRoleId(roleId: Long): Long {
        return userRoleMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_USER_ROLE)
                .where(SYS_USER_ROLE.ROLE_ID.eq(roleId))
        )
    }

    /**
     * 新增保存角色信息
     *
     * @param bo 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun insertRole(bo: SysRoleBo): Int {
        val role: SysRole = MapstructUtils.convert(bo, SysRole::class.java)!!
        // 新增角色信息
        baseMapper.insert(role, true)
        bo.roleId = role.roleId
        return insertRoleMenu(bo)
    }

    /**
     * 修改保存角色信息
     *
     * @param bo 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateRole(bo: SysRoleBo): Int {
        val role: SysRole = MapstructUtils.convert(bo, SysRole::class.java)!!
        // 修改角色信息
        baseMapper.update(role)
        // 删除角色与菜单关联
        roleMenuMapper.deleteByQuery(
            QueryWrapper.create().from(SYS_ROLE_MENU)
                .where(SYS_ROLE_MENU.ROLE_ID.eq(role.roleId))
        )
        return insertRoleMenu(bo)
    }

    /**
     * 修改角色状态
     *
     * @param roleId 角色ID
     * @param status 角色状态
     * @return 结果
     */
    override fun updateRoleStatus(roleId: Long, status: String): Boolean {
        if (UserConstants.ROLE_DISABLE == status && this.countUserRoleByRoleId(roleId) > 0) {
            throw ServiceException("角色已分配，不能禁用!")
        }
        return UpdateChain.of<Class<SysRole>>(SysRole::class.java)
            .set(SYS_ROLE.STATUS, status)
            .where(SYS_ROLE.ROLE_ID.eq(roleId))
            .update()
    }

    /**
     * 修改数据权限信息
     *
     * @param bo 角色信息
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun authDataScope(bo: SysRoleBo): Int {
        val role: SysRole = MapstructUtils.convert(bo, SysRole::class.java)!!
        // 修改角色信息
        baseMapper.update(role)
        // 删除角色与部门关联
        roleDeptMapper.deleteByQuery(
            QueryWrapper.create().from(SYS_ROLE_DEPT)
                .where(SYS_ROLE_DEPT.ROLE_ID.eq(role.roleId))
        )

        // 新增角色和部门信息（数据权限）
        return insertRoleDept(bo)
    }

    /**
     * 新增角色菜单信息
     *
     * @param role 角色对象
     */
    private fun insertRoleMenu(role: SysRoleBo): Int {
        var rows = 1
        // 新增用户与角色管理
        val list: MutableList<SysRoleMenu> = mutableListOf()
        for (menuId in role.menuIds!!) {
            val rm = SysRoleMenu()
            rm.roleId = role.roleId
            rm.menuId = menuId
            list.add(rm)
        }
        if (list.size > 0) {
            //rows = roleMenuMapper.executeBatch(list, SysRoleMenuMapper.class, BaseMapper::insertWithPk);
            rows = Arrays.stream(Db.executeBatch(list, 1000, SysRoleMenuMapper::class.java) { mapper, index ->
                mapper.insert(index)
            }).filter { it: Int -> it != 0 }.count().toInt()
        }
        return rows
    }

    /**
     * 新增角色部门信息(数据权限)
     *
     * @param role 角色对象
     */
    private fun insertRoleDept(role: SysRoleBo): Int {
        var rows = 1
        // 新增角色与部门（数据权限）管理
        val list: MutableList<SysRoleDept> = mutableListOf()
        for (deptId in role.deptIds!!) {
            val rd = SysRoleDept()
            rd.roleId = role.roleId
            rd.deptId = deptId
            list.add(rd)
        }
        if (list.size > 0) {
            //rows = roleDeptMapper.insertBatch(list);
            rows = Arrays.stream(Db.executeBatch(list, 1000, SysRoleDeptMapper::class.java) { mapper, index ->
                mapper.insert(index)
            }).filter { it: Int -> it != 0 }.count().toInt()
        }
        return rows
    }

    /**
     * 通过角色ID删除角色
     *
     * @param roleId 角色ID
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteRoleById(roleId: Long): Int {
        // 删除角色与菜单关联
        roleMenuMapper.deleteByQuery(
            QueryWrapper.create().from(SYS_ROLE_MENU)
                .where(SYS_ROLE_MENU.ROLE_ID.eq(roleId))
        )
        // 删除角色与部门关联
        roleDeptMapper.deleteByQuery(
            QueryWrapper.create().from(SYS_ROLE_DEPT)
                .where(SYS_ROLE_DEPT.ROLE_ID.eq(roleId))
        )
        return baseMapper.deleteById(roleId)
    }

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteRoleByIds(roleIds: Array<Long>): Int {
        for (roleId in roleIds) {
            val role = baseMapper.selectOneById(roleId)
            checkRoleAllowed(BeanUtil.toBean(role, SysRoleBo::class.java))
            checkRoleDataScope(roleId)
            if (countUserRoleByRoleId(roleId) > 0) {
                throw ServiceException("%1${role.roleName}已分配，不能删除!")
            }
        }
        val ids = listOf(*roleIds)
        // 删除角色与菜单关联
        roleMenuMapper.deleteByQuery(QueryWrapper.create().from(SYS_ROLE_MENU).where(SYS_ROLE_MENU.ROLE_ID.`in`(ids)))
        // 删除角色与部门关联
        roleDeptMapper.deleteByQuery(QueryWrapper.create().from(SYS_ROLE_DEPT).where(SYS_ROLE_DEPT.ROLE_ID.`in`(ids)))
        return baseMapper.deleteBatchByIds(ids)
    }

    /**
     * 取消授权用户角色
     *
     * @param userRole 用户和角色关联信息
     * @return 结果
     */
    override fun deleteAuthUser(userRole: SysUserRole): Int {
        val rows = userRoleMapper.deleteByQuery(
            QueryWrapper.create().from(SYS_USER_ROLE)
                .where(SYS_USER_ROLE.ROLE_ID.eq(userRole.roleId))
                .and(SYS_USER_ROLE.USER_ID.eq(userRole.userId))
        )
        if (rows > 0) {
            cleanOnlineUserByRole(userRole.roleId!!)
        }
        return rows
    }

    /**
     * 批量取消授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要取消授权的用户数据ID
     * @return 结果
     */
    override fun deleteAuthUsers(roleId: Long, userIds: Array<Long>): Int {
        val rows = userRoleMapper.deleteByQuery(
            QueryWrapper.create().from(SYS_USER_ROLE)
                .where(SYS_USER_ROLE.ROLE_ID.eq(roleId))
                .and(SYS_USER_ROLE.USER_ID.`in`(listOf(*userIds)))
        )
        if (rows > 0) {
            cleanOnlineUserByRole(roleId)
        }
        return rows
    }

    /**
     * 批量选择授权用户角色
     *
     * @param roleId  角色ID
     * @param userIds 需要授权的用户数据ID
     * @return 结果
     */
    override fun insertAuthUsers(roleId: Long, userIds: Array<Long>): Int {
        // 新增用户与角色管理
        var rows = 1
        val list: MutableList<SysUserRole> = StreamUtils.toList(listOf(*userIds)) { userId ->
            val ur = SysUserRole()
            ur.userId = userId
            ur.roleId = roleId
            ur
        }
        if (CollUtil.isNotEmpty(list)) {
            rows = Arrays.stream(Db.executeBatch(list, 1000, SysUserRoleMapper::class.java) { mapper, index ->
                mapper.insert(index)
            }).filter { it: Int -> it != 0 }.count().toInt()
        }
        if (rows > 0) {
            cleanOnlineUserByRole(roleId)
        }
        return rows
    }

    override fun cleanOnlineUserByRole(roleId: Long) {
        // 如果角色未绑定用户 直接返回
        val num = userRoleMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_USER_ROLE).where(SYS_USER_ROLE.ROLE_ID.eq(roleId))
        )
        if (num == 0L) {
            return
        }
        val keys = StpUtil.searchTokenValue("", 0, -1, false)
        if (CollUtil.isEmpty(keys)) {
            return
        }
        // 角色关联的在线用户量过大会导致redis阻塞卡顿 谨慎操作
        keys.parallelStream().forEach { key: String ->
            val token = StringUtils.substringAfterLast(key, ":")
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < -1) {
                return@forEach
            }
            val loginUser: LoginUser = LoginHelper.getLoginUser(token)!!
            if (loginUser.roles?.stream()?.anyMatch { r -> r.roleId == roleId } == true) {
                try {
                    StpUtil.logoutByTokenValue(token)
                } catch (ignored: NotLoginException) {
                }
            }
        }
    }

}
