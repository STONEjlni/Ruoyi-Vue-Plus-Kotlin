package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.lang.tree.Tree
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.StreamUtils.filter
import com.blank.common.core.utils.StringUtilsExtend.splitList
import com.blank.common.core.utils.TreeBuildUtils.build
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.system.domain.SysMenu
import com.blank.system.domain.SysRole
import com.blank.system.domain.bo.SysMenuBo
import com.blank.system.domain.table.SysMenuDef.SYS_MENU
import com.blank.system.domain.table.SysRoleDef.SYS_ROLE
import com.blank.system.domain.table.SysRoleMenuDef.SYS_ROLE_MENU
import com.blank.system.domain.table.SysUserRoleDef.SYS_USER_ROLE
import com.blank.system.domain.vo.MetaVo
import com.blank.system.domain.vo.RouterVo
import com.blank.system.domain.vo.SysMenuVo
import com.blank.system.mapper.SysMenuMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysRoleMenuMapper
import com.blank.system.service.ISysMenuService
import com.mybatisflex.core.query.QueryMethods.distinct
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service

/**
 * 菜单 业务层处理
 */
@Service
class SysMenuServiceImpl(
    private val baseMapper: SysMenuMapper,
    private val roleMapper: SysRoleMapper,
    private val roleMenuMapper: SysRoleMenuMapper
) : ISysMenuService {


    /**
     * 根据用户查询系统菜单列表
     *
     * @param userId 用户ID
     * @return 菜单列表
     */
    override fun selectMenuList(userId: Long): MutableList<SysMenuVo> {
        return selectMenuList(SysMenuBo(), userId)
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    override fun selectMenuList(menu: SysMenuBo, userId: Long): MutableList<SysMenuVo> {
        // 管理员显示所有菜单信息
        val menuList = if (isSuperAdmin(userId)) {
            baseMapper.selectListByQueryAs(
                QueryWrapper.create().from(SYS_MENU)
                    .where(SYS_MENU.MENU_NAME.like(menu.menuName))
                    .and(SYS_MENU.VISIBLE.eq(menu.visible))
                    .and(SYS_MENU.STATUS.eq(menu.status))
                    .orderBy(SYS_MENU.PARENT_ID, true)
                    .orderBy(SYS_MENU.ORDER_NUM, true),
                SysMenuVo::class.java
            )
        } else {
            val queryWrapper: QueryWrapper = QueryWrapper.create()
                .select(distinct(SYS_MENU.ALL_COLUMNS))
                .from(SYS_MENU)
                .leftJoin<QueryWrapper>(SYS_ROLE_MENU).on(SYS_MENU.MENU_ID.eq(SYS_ROLE_MENU.MENU_ID))
                .leftJoin<QueryWrapper>(SYS_USER_ROLE).on(SYS_ROLE_MENU.ROLE_ID.eq(SYS_USER_ROLE.ROLE_ID))
                .leftJoin<QueryWrapper>(SYS_ROLE).on(SYS_USER_ROLE.ROLE_ID.eq(SYS_ROLE.ROLE_ID))
                .where(SYS_USER_ROLE.USER_ID.eq(userId))
                .and(SYS_MENU.MENU_NAME.like(menu.menuName))
                .and(SYS_MENU.VISIBLE.like(menu.visible))
                .and(SYS_MENU.STATUS.like(menu.status))
                .orderBy(SYS_MENU.PARENT_ID, true)
                .orderBy(SYS_MENU.ORDER_NUM, true)
            val list = baseMapper.selectListByQueryAs(queryWrapper, SysMenu::class.java)
            convert(list, SysMenuVo::class.java)!!
        }
        return menuList
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    override fun selectMenuPermsByUserId(userId: Long): MutableSet<String> {
        val perms: MutableList<String> = baseMapper.selectMenuPermsByUserId(userId)
        val permsSet: MutableSet<String> = mutableSetOf()
        for (perm in perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(splitList(perm.trim { it <= ' ' }))
            }
        }
        return permsSet
    }

    /**
     * 根据角色ID查询权限
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    override fun selectMenuPermsByRoleId(roleId: Long): MutableSet<String> {
        val perms: MutableList<String> = baseMapper.selectMenuPermsByRoleId(roleId)
        val permsSet: MutableSet<String> = mutableSetOf()
        for (perm in perms) {
            if (StringUtils.isNotEmpty(perm)) {
                permsSet.addAll(splitList(perm.trim { it <= ' ' }))
            }
        }
        return permsSet
    }

    /**
     * 根据用户ID查询菜单
     *
     * @param userId 用户名称
     * @return 菜单列表
     */
    override fun selectMenuTreeByUserId(userId: Long): MutableList<SysMenu> {
        val menus = if (isSuperAdmin(userId)) {
            baseMapper.selectMenuTreeAll()
        } else {
            baseMapper.selectMenuTreeByUserId(userId)
        }
        return getChildPerms(menus, 0)
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    override fun selectMenuListByRoleId(roleId: Long): MutableList<Long> {
        val role: SysRole? = roleMapper.selectOneById(roleId)
        return baseMapper.selectMenuListByRoleId(roleId, role?.menuCheckStrictly!!)
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    override fun buildMenus(menus: MutableList<SysMenu>): MutableList<RouterVo> {
        val routers: MutableList<RouterVo> = mutableListOf()
        for (menu in menus) {
            val router = RouterVo()
            router.hidden = "1" == menu.visible
            router.name = menu.routeName
            router.path = menu.routerPath
            router.component = menu.componentInfo
            router.query = menu.queryParam
            router.meta = MetaVo(
                menu.menuName!!,
                menu.icon!!,
                StringUtils.equals("1", menu.isCache),
                menu.path
            )

            val cMenus: MutableList<SysMenu> = menu.children
            if (CollUtil.isNotEmpty(cMenus) && UserConstants.TYPE_DIR == menu.menuType) {
                router.alwaysShow = true
                router.redirect = "noRedirect"
                router.children = buildMenus(cMenus)
            } else if (menu.isMenuFrame) {
                router.meta = null
                val childrenList: MutableList<RouterVo> = mutableListOf()
                val children = RouterVo()
                children.path = menu.path
                children.component = menu.component
                children.name = StringUtils.capitalize(menu.path)
                children.meta = MetaVo(
                        menu.menuName!!,
                        menu.icon!!,
                        StringUtils.equals("1", menu.isCache),
                        menu.path
                    )
                children.query = menu.queryParam
                childrenList.add(children)
                router.children = childrenList
            } else if (menu.parentId?.toInt() == 0 && menu.isInnerLink) {
                router.meta = MetaVo(menu.menuName!!, menu.icon!!)
                router.path = "/"
                val childrenList: MutableList<RouterVo> = ArrayList()
                val children = RouterVo()
                val routerPath = SysMenu.innerLinkReplaceEach(menu.path)
                children.path = routerPath
                children.component = UserConstants.INNER_LINK
                children.name = StringUtils.capitalize(routerPath)
                children.meta = MetaVo(menu.menuName!!, menu.icon!!, menu.path)
                childrenList.add(children)
                router.children = childrenList
            }
            routers.add(router)
        }
        return routers
    }

    /**
     * 构建前端所需要下拉树结构
     *
     * @param menus 菜单列表
     * @return 下拉树结构列表
     */
    override fun buildMenuTreeSelect(menus: MutableList<SysMenuVo>): MutableList<Tree<Long>> {
        return if (CollUtil.isEmpty(menus)) {
            CollUtil.newArrayList()
        } else build(menus) { menu, tree ->
            tree.setId(menu.menuId)
                .setParentId(menu.parentId)
                .setName(menu.menuName)
                .setWeight(menu.orderNum)
        }
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    override fun selectMenuById(menuId: Long): SysMenuVo? {
        return baseMapper.selectOneWithRelationsByIdAs(menuId, SysMenuVo::class.java)
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    override fun hasChildByMenuId(menuId: Long): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_MENU).where(SYS_MENU.PARENT_ID.eq(menuId))
        ) > 0
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    override fun checkMenuExistRole(menuId: Long): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_ROLE_MENU).where(SYS_ROLE_MENU.MENU_ID.eq(menuId))
        ) > 0
    }

    /**
     * 新增保存菜单信息
     *
     * @param bo 菜单信息
     * @return 结果
     */
    override fun insertMenu(bo: SysMenuBo): Int {
        val menu: SysMenu? = convert(bo, SysMenu::class.java)
        return baseMapper.insert(menu, true)
    }

    /**
     * 修改保存菜单信息
     *
     * @param bo 菜单信息
     * @return 结果
     */
    override fun updateMenu(bo: SysMenuBo): Int {
        val menu: SysMenu? = convert(bo, SysMenu::class.java)
        return baseMapper.update(menu)
    }

    /**
     * 删除菜单管理信息
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    override fun deleteMenuById(menuId: Long): Int {
        return baseMapper.deleteById(menuId)
    }

    /**
     * 校验菜单名称是否唯一
     *
     * @param menu 菜单信息
     * @return 结果
     */
    override fun checkMenuNameUnique(menu: SysMenuBo): Boolean {
        return baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_MENU).where(SYS_MENU.MENU_NAME.eq(menu.menuName))
                .and(SYS_MENU.PARENT_ID.eq(menu.parentId))
                .and(SYS_MENU.MENU_ID.ne(menu.menuId))
        ) == 0.toLong()
    }

    /**
     * 根据父节点的ID获取所有子节点
     *
     * @param list     分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    private fun getChildPerms(list: MutableList<SysMenu>, parentId: Int): MutableList<SysMenu> {
        val returnList: MutableList<SysMenu> = ArrayList()
        for (t in list) {
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.parentId == parentId.toLong()) {
                recursionFn(list, t)
                returnList.add(t)
            }
        }
        return returnList
    }

    /**
     * 递归列表
     */
    private fun recursionFn(list: MutableList<SysMenu>, t: SysMenu) {
        // 得到子节点列表
        val childList: MutableList<SysMenu> = filter(list) { n ->
            n.parentId?.equals(t.menuId)!!
        }
        t.children = childList
        for (tChild in childList) {
            // 判断是否有子节点
            if (list.stream().anyMatch { n: SysMenu ->
                    n.parentId?.equals(tChild.menuId)!!
                }) {
                recursionFn(list, tChild)
            }
        }
    }
}
