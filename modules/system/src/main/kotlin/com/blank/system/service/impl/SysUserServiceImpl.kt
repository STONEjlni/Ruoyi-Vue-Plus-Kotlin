package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.UserService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.StreamUtils.join
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.system.domain.SysUser
import com.blank.system.domain.bo.SysUserBo
import com.blank.system.domain.vo.SysRoleVo
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysDeptMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysUserMapper
import com.blank.system.mapper.SysUserRoleMapper
import com.blank.system.service.ISysUserService
import com.mybatisflex.core.query.QueryWrapper
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

    override fun selectPageUserList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>? {
        /*Page<SysUserVo> page = baseMapper.selectPageUserList(pageQuery.build(), this.buildQueryWrapper(user));
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 根据条件分页查询用户列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    override fun selectUserList(user: SysUserBo): MutableList<SysUserVo>? {
        /*return baseMapper.selectUserList(this.buildQueryWrapper(user));*/
        return null
    }

    private fun  /*<SysUser>*/buildQueryWrapper(user: SysUserBo): QueryWrapper? {
        /*Map<String, Object> params = user.getParams();
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .eq(ObjectUtil.isNotNull(user.getUserId()), "u.user_id", user.getUserId())
                .like(StrUtil.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(StrUtil.isNotBlank(user.getStatus()), "u.status", user.getStatus())
                .like(StrUtil.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        "u.create_time", params.get("beginTime"), params.get("endTime"))
                .and(ObjectUtil.isNotNull(user.getDeptId()), w -> {
                    List<SysDept> deptList = deptMapper.selectList(new LambdaQueryWrapper<SysDept>()
                            .select(SysDept::getDeptId)
                            .apply(DataBaseHelper.findInSet(user.getDeptId(), "ancestors")));
                    List<Long> ids = StreamUtils.toList(deptList, SysDept::getDeptId);
                    ids.add(user.getDeptId());
                    w.in("u.dept_id", ids);
                }).orderByAsc("u.user_id");
        return wrapper;*/
        return null
    }

    /**
     * 根据条件分页查询已分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    override fun selectAllocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>? {
        /*QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .eq(ObjectUtil.isNotNull(user.getRoleId()), "r.role_id", user.getRoleId())
                .like(StrUtil.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .eq(StrUtil.isNotBlank(user.getStatus()), "u.status", user.getStatus())
                .like(StrUtil.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
                .orderByAsc("u.user_id");
        Page<SysUserVo> page = baseMapper.selectAllocatedList(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 根据条件分页查询未分配用户角色列表
     *
     * @param user 用户信息
     * @return 用户信息集合信息
     */
    override fun selectUnallocatedList(user: SysUserBo, pageQuery: PageQuery): TableDataInfo<SysUserVo>? {
        /*List<Long> userIds = userRoleMapper.selectUserIdsByRoleId(user.getRoleId());
        QueryWrapper<SysUser> wrapper = Wrappers.query();
        wrapper.eq("u.del_flag", UserConstants.USER_NORMAL)
                .and(w -> w.ne("r.role_id", user.getRoleId()).or().isNull("r.role_id"))
                .notIn(CollUtil.isNotEmpty(userIds), "u.user_id", userIds)
                .like(StrUtil.isNotBlank(user.getUserName()), "u.user_name", user.getUserName())
                .like(StrUtil.isNotBlank(user.getPhonenumber()), "u.phonenumber", user.getPhonenumber())
                .orderByAsc("u.user_id");
        Page<SysUserVo> page = baseMapper.selectUnallocatedList(pageQuery.build(), wrapper);
        return TableDataInfo.build(page);*/
        return null
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
        val list = roleMapper.selectRolesByUserName(userName)!!
        return if (CollUtil.isEmpty(list)) {
            StringUtils.EMPTY
        } else join(list) { obj: SysRoleVo -> obj.roleName!! }
    }

    /**
     * 查询用户所属岗位组
     *
     * @param userName 用户名
     * @return 结果
     */
    override fun selectUserPostGroup(userName: String): String? {
        /*List<SysPostVo> list = postMapper.selectPostsByUserName(userName);
        if (CollUtil.isEmpty(list)) {
            return StringUtils.EMPTY;
        }
        return StreamUtils.join(list, SysPostVo::getPostName);*/
        return null
    }

    /**
     * 校验用户名称是否唯一
     *
     * @param user 用户信息
     * @return 结果
     */
    override fun checkUserNameUnique(user: SysUserBo): Boolean {
        /*boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getUserName, user.getUserName())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;*/
        return false
    }

    /**
     * 校验手机号码是否唯一
     *
     * @param user 用户信息
     */
    override fun checkPhoneUnique(user: SysUserBo): Boolean {
        /*boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getPhonenumber, user.getPhonenumber())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;*/
        return false
    }

    /**
     * 校验email是否唯一
     *
     * @param user 用户信息
     */
    override fun checkEmailUnique(user: SysUserBo): Boolean {
        /*boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getEmail, user.getEmail())
                .ne(ObjectUtil.isNotNull(user.getUserId()), SysUser::getUserId, user.getUserId()));
        return !exist;*/
        return false
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
        if (ObjectUtil.isNull(baseMapper.selectUserById(userId))) {
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
        val sysUser = convert(user, SysUser::class.java)!!
        // 新增用户信息
        val rows = baseMapper.insert(sysUser)
        user.userId = sysUser.userId
        // 新增用户岗位关联
        insertUserPost(user, false)
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
        val sysUser = convert(user, SysUser::class.java)!!
        return baseMapper.insert(sysUser) > 0
    }

    /**
     * 修改保存用户信息
     *
     * @param user 用户信息
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateUser(user: SysUserBo): Int {
        /*// 新增用户与角色管理
        insertUserRole(user, true);
        // 新增用户与岗位管理
        insertUserPost(user, true);
        SysUser sysUser = MapstructUtils.convert(user, SysUser.class);
        // 防止错误更新后导致的数据误删除
        int flag = baseMapper.updateById(sysUser);
        if (flag < 1) {
            throw new ServiceException("修改用户" + user.getUserName() + "信息失败");
        }
        return flag;*/
        return 0
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
    override fun updateUserStatus(userId: Long, status: String): Int {
        /*return baseMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .set(SysUser::getStatus, status)
                        .eq(SysUser::getUserId, userId));*/
        return 0
    }

    /**
     * 修改用户基本信息
     *
     * @param user 用户信息
     * @return 结果
     */
    override fun updateUserProfile(user: SysUserBo): Int {
        /*return baseMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .set(ObjectUtil.isNotNull(user.getNickName()), SysUser::getNickName, user.getNickName())
                        .set(SysUser::getPhonenumber, user.getPhonenumber())
                        .set(SysUser::getEmail, user.getEmail())
                        .set(SysUser::getSex, user.getSex())
                        .eq(SysUser::getUserId, user.getUserId()));*/
        return 0
    }

    /**
     * 修改用户头像
     *
     * @param userId 用户ID
     * @param avatar 头像地址
     * @return 结果
     */
    override fun updateUserAvatar(userId: Long, avatar: Long): Boolean {
        /*return baseMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .set(SysUser::getAvatar, avatar)
                        .eq(SysUser::getUserId, userId)) > 0;*/
        return false
    }

    /**
     * 重置用户密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    override fun resetUserPwd(userId: Long, password: String): Int {
        /*return baseMapper.update(null,
                new LambdaUpdateWrapper<SysUser>()
                        .set(SysUser::getPassword, password)
                        .eq(SysUser::getUserId, userId));*/
        return 0
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
     * 新增用户岗位信息
     *
     * @param user  用户对象
     * @param clear 清除已存在的关联数据
     */
    private fun insertUserPost(user: SysUserBo, clear: Boolean) {
        /*Long[] posts = user.getPostIds();
        if (ArrayUtil.isNotEmpty(posts)) {
            if (clear) {
                // 删除用户与岗位关联
                userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, user.getUserId()));
            }
            // 新增用户与岗位管理
            List<SysUserPost> list = StreamUtils.toList(List.of(posts), postId -> {
                SysUserPost up = new SysUserPost();
                up.setUserId(user.getUserId());
                up.setPostId(postId);
                return up;
            });
            userPostMapper.insertBatch(list);
        }*/
    }

    /**
     * 新增用户角色信息
     *
     * @param userId  用户ID
     * @param roleIds 角色组
     * @param clear   清除已存在的关联数据
     */
    private fun insertUserRole(userId: Long, roleIds: Array<Long>, clear: Boolean) {
        /*if (ArrayUtil.isNotEmpty(roleIds)) {
            // 判断是否具有此角色的操作权限
            List<SysRoleVo> roles = roleMapper.selectRoleList(new LambdaQueryWrapper<>());
            if (CollUtil.isEmpty(roles)) {
                throw new ServiceException("没有权限访问角色的数据");
            }
            List<Long> roleList = StreamUtils.toList(roles, SysRoleVo::getRoleId);
            if (!LoginHelper.isSuperAdmin(userId)) {
                roleList.remove(UserConstants.SUPER_ADMIN_ID);
            }
            List<Long> canDoRoleList = StreamUtils.filter(List.of(roleIds), roleList::contains);
            if (CollUtil.isEmpty(canDoRoleList)) {
                throw new ServiceException("没有权限访问角色的数据");
            }
            if (clear) {
                // 删除用户与角色关联
                userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
            }
            // 新增用户与角色管理
            List<SysUserRole> list = StreamUtils.toList(canDoRoleList, roleId -> {
                SysUserRole ur = new SysUserRole();
                ur.setUserId(userId);
                ur.setRoleId(roleId);
                return ur;
            });
            userRoleMapper.insertBatch(list);
        }*/
    }

    /**
     * 通过用户ID删除用户
     *
     * @param userId 用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteUserById(userId: Long): Int {
        /* // 删除用户与角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().eq(SysUserRole::getUserId, userId));
        // 删除用户与岗位表
        userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().eq(SysUserPost::getUserId, userId));
        // 防止更新失败导致的数据删除
        int flag = baseMapper.deleteById(userId);
        if (flag < 1) {
            throw new ServiceException("删除用户失败!");
        }
        return flag;*/
        return 0
    }

    /**
     * 批量删除用户信息
     *
     * @param userIds 需要删除的用户ID
     * @return 结果
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun deleteUserByIds(userIds: Array<Long>): Int {
        /*for (Long userId : userIds) {
            checkUserAllowed(userId);
            checkUserDataScope(userId);
        }
        List<Long> ids = List.of(userIds);
        // 删除用户与角色关联
        userRoleMapper.delete(new LambdaQueryWrapper<SysUserRole>().in(SysUserRole::getUserId, ids));
        // 删除用户与岗位表
        userPostMapper.delete(new LambdaQueryWrapper<SysUserPost>().in(SysUserPost::getUserId, ids));
        // 防止更新失败导致的数据删除
        int flag = baseMapper.deleteBatchIds(ids);
        if (flag < 1) {
            throw new ServiceException("删除用户失败!");
        }
        return flag;*/
        return 0
    }

    /**
     * 通过部门id查询当前部门所有用户
     *
     * @param deptId
     * @return
     */
    override fun selectUserListByDept(deptId: Long): MutableList<SysUserVo>? {
        /*LambdaQueryWrapper<SysUser> lqw = Wrappers.lambdaQuery();
        lqw.eq(SysUser::getDeptId, deptId);
        lqw.orderByAsc(SysUser::getUserId);
        return baseMapper.selectVoList(lqw);*/
        return null
    }

    @Cacheable(cacheNames = [CacheNames.SYS_USER_NAME], key = "#userId")
    override fun selectUserNameById(userId: Long): String? {
        /*SysUser sysUser = baseMapper.selectOne(new LambdaQueryWrapper<SysUser>()
                .select(SysUser::getUserName).eq(SysUser::getUserId, userId));
        return ObjectUtil.isNull(sysUser) ? null : sysUser.getUserName();*/
        return null
    }
}
