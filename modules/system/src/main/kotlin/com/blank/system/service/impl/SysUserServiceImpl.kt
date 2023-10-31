package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.UserService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.StreamUtils
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.mybatis.helper.DataBaseHelper
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.system.domain.SysDept
import com.blank.system.domain.SysUser
import com.blank.system.domain.SysUserRole
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.domain.table.SysDeptDef.SYS_DEPT
import com.blank.system.domain.table.SysRoleDef.SYS_ROLE
import com.blank.system.domain.table.SysUserDef.SYS_USER
import com.blank.system.domain.table.SysUserRoleDef
import com.blank.system.domain.table.SysUserRoleDef.SYS_USER_ROLE
import com.blank.system.domain.vo.SysRoleVo
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysDeptMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysUserMapper
import com.blank.system.mapper.SysUserRoleMapper
import com.blank.system.service.ISysUserService
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.If
import com.mybatisflex.core.query.QueryMethods
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.row.Db
import com.mybatisflex.core.util.UpdateEntity
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 用户 业务层处理
 */
@Slf4j
@Service
class SysUserServiceImpl(
    private val baseMapper: SysUserMapper,
    private val deptMapper: SysDeptMapper,
    private val roleMapper: SysRoleMapper,
    private val userRoleMapper: SysUserRoleMapper
) : ISysUserService, UserService {

    override fun selectPageUserList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo> {
        val page = selectPageUserList(pageQuery, buildQueryWrapper(user))
        return TableDataInfo.build(page)
    }

    private fun selectPageUserList(pageQuery: PageQuery, queryWrapper: QueryWrapper): Page<SysUserVo> {
        queryWrapper
            .select(
                SYS_USER.USER_ID,
                SYS_USER.DEPT_ID,
                SYS_USER.USER_NAME,
                SYS_USER.NICK_NAME,
                SYS_USER.EMAIL,
                SYS_USER.AVATAR,
                SYS_USER.PHONENUMBER,
                SYS_USER.SEX,
                SYS_USER.STATUS,
                SYS_USER.DEL_FLAG,
                SYS_USER.LOGIN_IP,
                SYS_USER.LOGIN_DATE,
                SYS_USER.CREATE_BY,
                SYS_USER.CREATE_TIME,
                SYS_USER.REMARK,
                SYS_DEPT.DEPT_NAME,
                SYS_DEPT.LEADER,
                QueryMethods.column("u1.user_name as leaderName")
            )
            .leftJoin<QueryWrapper>(SYS_DEPT).`as`("d").on(SYS_USER.DEPT_ID.eq(SYS_DEPT.DEPT_ID))
            .leftJoin<QueryWrapper>(SYS_USER).`as`("u1").on(SYS_USER.USER_ID.eq(SYS_DEPT.LEADER))
        return baseMapper.selectPageUserList(pageQuery, queryWrapper)
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    override fun selectUserList(user: SysUserBo): MutableList<SysUserVo> {
        return baseMapper.selectUserList(buildQueryWrapper(user))
    }

    private fun buildQueryWrapper(user: SysUserBo): QueryWrapper {
        val params: Map<String, Any> = user.params
        val queryWrapper: QueryWrapper = QueryWrapper.create().from(SYS_USER.`as`("u"))
            .where(SYS_USER.DEL_FLAG.eq(UserConstants.USER_NORMAL))
            .and(SYS_USER.USER_ID.eq(user.userId))
            .and(SYS_USER.USER_NAME.like(user.userName))
            .and(SYS_USER.STATUS.eq(user.status))
            .and(SYS_USER.PHONENUMBER.eq(user.phonenumber))
            .and(
                SYS_USER.CREATE_TIME.between(
                    params["beginTime"],
                    params["endTime"],
                    params["beginTime"] != null && params["endTime"] != null
                )
            )
        if (ObjectUtil.isNotNull(user.deptId)) {
            val deptList: MutableList<SysDept> = deptMapper.selectListByQuery(
                QueryWrapper.create().select(SYS_DEPT.DEPT_ID).from(SYS_DEPT)
                    .and(DataBaseHelper.findInSet(user.deptId!!, "ancestors"))
            )
            val ids: MutableList<Long?> = StreamUtils.toList(deptList, SysDept::deptId)
            ids.add(user.deptId)
            queryWrapper.and(SYS_USER.DEPT_ID.`in`(ids))
        }
        queryWrapper.orderBy(SYS_USER.USER_ID, true)
        return queryWrapper
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    override fun selectAllocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo> {
        val queryWrapper: QueryWrapper = QueryWrapper.create()
            .select(
                QueryMethods.distinct(
                    SYS_USER.USER_ID,
                    SYS_USER.DEPT_ID,
                    SYS_USER.USER_NAME,
                    SYS_USER.NICK_NAME,
                    SYS_USER.EMAIL,
                    SYS_USER.PHONENUMBER,
                    SYS_USER.STATUS,
                    SYS_USER.CREATE_TIME
                )
            )
            .from(SYS_USER).`as`("u")
            .leftJoin<QueryWrapper>(SYS_DEPT).`as`("d").on(SYS_USER.DEPT_ID.eq(SYS_DEPT.DEPT_ID))
            .leftJoin<QueryWrapper>(SYS_USER_ROLE).on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
            .leftJoin<QueryWrapper>(SYS_ROLE).on(SYS_ROLE.ROLE_ID.eq(SYS_USER_ROLE.ROLE_ID))
            .where(SYS_USER.DEL_FLAG.eq(UserConstants.USER_NORMAL))
            .and(SYS_ROLE.ROLE_ID.eq(user.roleId))
            .and(SYS_USER.USER_NAME.like(user.userName))
            .and(SYS_USER.STATUS.eq(user.status))
            .and(SYS_USER.PHONENUMBER.eq(user.phonenumber))
            .orderBy(SYS_USER.USER_ID, true)
        val page = baseMapper.selectAllocatedList(pageQuery, queryWrapper)
        return TableDataInfo.build(page)
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    override fun selectUnallocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo> {
        val userIds: MutableList<Long> = userRoleMapper.selectUserIdsByRoleId(user.roleId!!)
        val queryWrapper: QueryWrapper = QueryWrapper.create()
            .select(
                QueryMethods.distinct(
                    SYS_USER.USER_ID,
                    SYS_USER.DEPT_ID,
                    SYS_USER.USER_NAME,
                    SYS_USER.NICK_NAME,
                    SYS_USER.EMAIL,
                    SYS_USER.PHONENUMBER,
                    SYS_USER.STATUS,
                    SYS_USER.CREATE_TIME
                )
            )
            .from(SYS_USER).`as`("u")
            .leftJoin<QueryWrapper>(SYS_DEPT).`as`("d").on(SYS_USER.DEPT_ID.eq(SYS_DEPT.DEPT_ID))
            .leftJoin<QueryWrapper>(SYS_USER_ROLE).on(SYS_USER.USER_ID.eq(SYS_USER_ROLE.USER_ID))
            .leftJoin<QueryWrapper>(SYS_ROLE).on(SYS_ROLE.ROLE_ID.eq(SYS_USER_ROLE.ROLE_ID))
            .where(SYS_USER.DEL_FLAG.eq(UserConstants.USER_NORMAL))
            .and(SYS_ROLE.ROLE_ID.eq(user.roleId).or(SYS_ROLE.ROLE_ID.isNull()))
            .and(SYS_USER.USER_ID.notIn(userIds, If::isNotEmpty))
            .and(SYS_USER.USER_NAME.like(user.userName))
            .and(SYS_USER.PHONENUMBER.eq(user.phonenumber))
            .orderBy(SYS_USER.USER_ID, true)
        val page = baseMapper.selectAllocatedList(pageQuery, queryWrapper)
        return TableDataInfo.build(page)
    }

    /**
     * 通过用户名查询用户
     *
     * @param userName 用户名
     * @return 用户对象信息
     */
    override fun selectUserByUserName(userName: String): SysUserVo? {
        return baseMapper.selectUserByUserName(userName)
    }

    /**
     * 通过手机号查询用户
     *
     * @param phonenumber 手机号
     * @return 用户对象信息
     */
    override fun selectUserByPhonenumber(phonenumber: String): SysUserVo? {
        return baseMapper.selectUserByPhonenumber(phonenumber)
    }

    /**
     * 通过用户ID查询用户
     *
     * @param userId 用户ID
     * @return 用户对象信息
     */
    override fun selectUserById(userId: Long): SysUserVo? {
        return baseMapper.selectUserById(userId)
    }

    /**
     * 查询用户所属角色组
     *
     * @param userName 用户名
     * @return 结果
     */
    override fun selectUserRoleGroup(userName: String): String? {
        val list: MutableList<SysRoleVo> = roleMapper.selectRolesByUserName(userName)
        return if (CollUtil.isEmpty(list)) {
            StringUtils.EMPTY
        } else list.joinToString { it.roleName!! }
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    override fun checkUserNameUnique(user: SysUserBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_USER)
                .where(SYS_USER.USER_NAME.eq(user.userName)).and(SYS_USER.USER_ID.ne(user.userId))
        ) == 0.toLong()
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     */
    override fun checkPhoneUnique(user: SysUserBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_USER)
                .where(SYS_USER.PHONENUMBER.eq(user.phonenumber)).and(SYS_USER.USER_ID.ne(user.userId))
        ) == 0.toLong()
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     */
    override fun checkEmailUnique(user: SysUserBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_USER)
                .where(SYS_USER.EMAIL.eq(user.email)).and(SYS_USER.USER_ID.ne(user.userId))
        ) == 0.toLong()
    }

    /**
     * 校验用户是否允许操作
     *
     * @param userId 用户ID
     */
    override fun checkUserAllowed(userId: Long) {
        if (ObjectUtil.isNotNull(userId) && isSuperAdmin(userId)) {
            throw ServiceException("不允许操作超级管理员用户")
        }
    }

    /**
     * 校验用户是否有数据权限
     *
     * @param userId 用户id
     */
    override fun checkUserDataScope(userId: Long) {
        if (ObjectUtil.isNull(userId)) {
            return
        }
        if (isSuperAdmin()) {
            return
        }
        if (ObjectUtil.isNull(selectUserById(userId))) {
            throw ServiceException("没有权限访问用户数据！")
        }
    }

    /**
     * 新增保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun insertUser(user: SysUserBo): Int {
        val sysUser: SysUser = convert(user, SysUser::class.java)!!
        // 新增用户信息
        val rows = baseMapper.insert(sysUser, true)
        user.userId = sysUser.userId
        // 新增用户与角色管理
        insertUserRole(user, false)
        return rows
    }

    /**
     * 注册用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    override fun registerUser(user: SysUserBo): Boolean {
        user.createBy = user.userId
        user.updateBy = user.userId
        val sysUser: SysUser? = convert(user, SysUser::class.java)
        return baseMapper.insert(sysUser, true) > 0
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateUser(user: SysUserBo): Int {
        // 新增用户与角色管理
        insertUserRole(user, true)
        if (StringUtils.isBlank(user.password)) {
            user.password = null
        }
        val sysUser: SysUser = convert(user, SysUser::class.java)!!
        // 防止错误更新后导致的数据误删除
        val flag = baseMapper.update(sysUser)
        if (flag < 1) {
            throw ServiceException("修改用户${user.userName}信息失败")
        }
        return flag
    }

    /**
     * 用户授权角色
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun insertUserAuth(userId: Long, roleIds: Array<Long>) {
        insertUserRole(userId, roleIds, true)
    }

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 帐号状态
     * @return 结果
     */
    override fun updateUserStatus(userId: Long, status: String): Boolean {
        val sysUser = UpdateEntity.of(SysUser::class.java, userId)
        sysUser.status = status
        return baseMapper.update(sysUser, true) > 0
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    override fun updateUserProfile(user: SysUserBo): Int {
        val sysUser = UpdateEntity.of(SysUser::class.java, user.userId)
        if (StringUtils.isNotBlank(user.nickName)) {
            sysUser.nickName = user.nickName
        }
        sysUser.phonenumber = user.phonenumber
        sysUser.email = user.email
        sysUser.sex = user.sex
        return baseMapper.update(sysUser, true)
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    override fun updateUserAvatar(userId: Long, avatar: Long): Boolean {
        val sysUser = UpdateEntity.of(SysUser::class.java, userId)
        sysUser.avatar = avatar
        return baseMapper.update(sysUser, true) > 0
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    override fun resetUserPwd(userId: Long, password: String): Boolean {
        val sysUser = UpdateEntity.of(SysUser::class.java, userId)
        sysUser.password = password
        return baseMapper.update(sysUser, true) > 0
    }

    /**
     * 新增用户角色信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private fun insertUserRole(user: SysUserBo, clear: Boolean) {
        this.insertUserRole(user.userId!!, user.roleIds!!, clear)
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     * @param clear   清除已存在的关联数据
     */
    private fun insertUserRole(userId: Long, roleIds: Array<Long>, clear: Boolean) {
        if (ArrayUtil.isNotEmpty(roleIds)) {
            // 判断是否具有此角色的操作权限
            val roles: MutableList<SysRoleVo> = roleMapper.selectRoleList(QueryWrapper.create())
            if (CollUtil.isEmpty(roles)) {
                throw ServiceException("没有权限访问角色的数据")
            }
            val roleList: MutableList<Long?> = StreamUtils.toList(roles, SysRoleVo::roleId)
            if (!isSuperAdmin(userId)) {
                roleList.remove(UserConstants.SUPER_ADMIN_ID)
            }
            val canDoRoleList: MutableList<Long?> = StreamUtils.filter(listOf(*roleIds), roleList::contains)
            if (CollUtil.isEmpty(canDoRoleList)) {
                throw ServiceException("没有权限访问角色的数据")
            }
            if (clear) {
                // 删除用户与角色关联
                userRoleMapper.deleteByQuery(
                    QueryWrapper().from(SysUserRole::class.java).where(SYS_USER_ROLE.USER_ID.eq(userId))
                )
            }
            // 新增用户与角色管理
            val list: MutableList<SysUserRole> = StreamUtils.toList(canDoRoleList) { roleId ->
                val ur = SysUserRole()
                ur.userId = userId
                ur.roleId = roleId
                ur
            }
            Db.executeBatch(list, 1000, SysUserRoleMapper::class.java) { mapper, index ->
                mapper.insert(index)
            }
        }
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteUserById(userId: Long): Int {
        // 删除用户与角色关联
        userRoleMapper.deleteByQuery(
            QueryWrapper().from(SysUserRole::class.java).where(SYS_USER_ROLE.USER_ID.eq(userId))
        )

        // 防止更新失败导致的数据删除
        val flag = baseMapper.deleteById(userId)
        if (flag < 1) {
            throw ServiceException("删除用户失败!")
        }
        return flag
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteUserByIds(userIds: Array<Long>): Int {
        for (userId in userIds) {
            checkUserAllowed(userId)
            checkUserDataScope(userId)
        }
        val ids = listOf(*userIds)
        // 删除用户与角色关联
        userRoleMapper.deleteByQuery(
            QueryWrapper().from(SysUserRole::class.java).where(SYS_USER_ROLE.USER_ID.`in`(ids))
        )

        // 防止更新失败导致的数据删除
        val flag = baseMapper.deleteBatchByIds(ids)
        if (flag < 1) {
            throw ServiceException("删除用户失败!")
        }
        return flag
    }

    /**
     * 通过部门id查询当前部门所有用户
     *
     * @param deptId
     * @return
     */
    override fun selectUserListByDept(deptId: Long): MutableList<SysUserVo> {
        val queryWrapper: QueryWrapper = QueryWrapper.create().from(SysUser::class.java)
            .where(SYS_USER.DEPT_ID.eq(deptId))
            .orderBy(SYS_USER.USER_ID, true)
        return baseMapper.selectListByQueryAs(queryWrapper, SysUserVo::class.java)
    }

    @Cacheable(cacheNames = [CacheNames.SYS_USER_NAME], key = "#userId")
    override fun selectUserNameById(userId: Long): String? {
        val sysUser = baseMapper.selectOneByQuery(
            QueryWrapper.create().select(SYS_USER.USER_NAME).from(SysUser::class.java)
                .where(SYS_USER.USER_ID.eq(userId))
        )
        return if (ObjectUtil.isNull(sysUser)) null else sysUser.userName
    }
}
