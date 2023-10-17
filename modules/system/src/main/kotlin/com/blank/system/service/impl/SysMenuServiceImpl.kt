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
import com.blank.system.domain.bo.SysMenuBo
import com.blank.system.domain.vo.MetaVo
import com.blank.system.domain.vo.RouterVo
import com.blank.system.domain.vo.SysMenuVo
import com.blank.system.mapper.SysMenuMapper
import com.blank.system.mapper.SysRoleMapper
import com.blank.system.mapper.SysRoleMenuMapper
import com.blank.system.service.ISysMenuService
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import java.util.*
import java.util.function.Predicate

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
    override fun selectMenuList(userId: Long): MutableList<SysMenuVo>? {
        return selectMenuList(SysMenuBo(), userId)
    }

    /**
     * 查询系统菜单列表
     *
     * @param menu 菜单信息
     * @return 菜单列表
     */
    override fun selectMenuList(menu: SysMenuBo, userId: Long): MutableList<SysMenuVo>? {
        /*List<SysMenuVo> menuList;
        // 管理员显示所有菜单信息
        if (LoginHelper.isSuperAdmin(userId)) {
            menuList = baseMapper.selectVoList(new LambdaQueryWrapper<SysMenu>()
                .like(StrUtil.isNotBlank(menu.getMenuName()), SysMenu::getMenuName, menu.getMenuName())
                .eq(StrUtil.isNotBlank(menu.getVisible()), SysMenu::getVisible, menu.getVisible())
                .eq(StrUtil.isNotBlank(menu.getStatus()), SysMenu::getStatus, menu.getStatus())
                .orderByAsc(SysMenu::getParentId)
                .orderByAsc(SysMenu::getOrderNum));
        } else {
            QueryWrapper<SysMenu> wrapper = Wrappers.query();
            wrapper.eq("sur.user_id", userId)
                .like(StrUtil.isNotBlank(menu.getMenuName()), "m.menu_name", menu.getMenuName())
                .eq(StrUtil.isNotBlank(menu.getVisible()), "m.visible", menu.getVisible())
                .eq(StrUtil.isNotBlank(menu.getStatus()), "m.status", menu.getStatus())
                .orderByAsc("m.parent_id")
                .orderByAsc("m.order_num");
            List<SysMenu> list = baseMapper.selectMenuListByUserId(wrapper);
            menuList = MapstructUtils.convert(list, SysMenuVo.class);
        }
        return menuList;*/
        return null
    }

    /**
     * 根据用户ID查询权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    override fun selectMenuPermsByUserId(userId: Long): MutableSet<String>? {
        val perms = baseMapper.selectMenuPermsByUserId(userId)
        val permsSet: MutableSet<String> = HashSet()
        for (perm in perms!!) {
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
    override fun selectMenuPermsByRoleId(roleId: Long): MutableSet<String>? {
        val perms = baseMapper.selectMenuPermsByRoleId(roleId)
        val permsSet: MutableSet<String> = HashSet()
        for (perm in perms!!) {
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
    override fun selectMenuTreeByUserId(userId: Long): MutableList<SysMenu>? {
        val menus: MutableList<SysMenu> = if (isSuperAdmin(userId)) {
            baseMapper.selectMenuTreeAll()!!
        } else {
            baseMapper.selectMenuTreeByUserId(userId)!!
        }
        return getChildPerms(menus, 0)
    }

    /**
     * 根据角色ID查询菜单树信息
     *
     * @param roleId 角色ID
     * @return 选中菜单列表
     */
    override fun selectMenuListByRoleId(roleId: Long): MutableList<Long>? {
        /*SysRole role = roleMapper.selectById(roleId);
        return baseMapper.selectMenuListByRoleId(roleId, role.getMenuCheckStrictly());*/
        return null
    }

    /**
     * 根据租户套餐ID查询菜单树信息
     *
     * @param packageId 租户套餐ID
     * @return 选中菜单列表
     */
    override fun selectMenuListByPackageId(packageId: Long): MutableList<Long>? {
        /*SysTenantPackage tenantPackage = tenantPackageMapper.selectById(packageId);
        List<Long> menuIds = StringUtils.splitTo(tenantPackage.getMenuIds(), Convert::toLong);
        if (CollUtil.isEmpty(menuIds)) {
            return List.of();
        }
        List<Long> parentIds = null;
        if (tenantPackage.getMenuCheckStrictly()) {
            parentIds = baseMapper.selectObjs(new LambdaQueryWrapper<SysMenu>()
                .select(SysMenu::getParentId)
                .in(SysMenu::getMenuId, menuIds), Convert::toLong);
        }
        return baseMapper.selectObjs(new LambdaQueryWrapper<SysMenu>()
            .in(SysMenu::getMenuId, menuIds)
            .notIn(CollUtil.isNotEmpty(parentIds), SysMenu::getMenuId, parentIds), Convert::toLong);*/
        return null
    }

    /**
     * 构建前端路由所需要的菜单
     *
     * @param menus 菜单列表
     * @return 路由列表
     */
    override fun buildMenus(menus: MutableList<SysMenu>): MutableList<RouterVo>? {
        val routers: MutableList<RouterVo> = LinkedList()
        for (menu in menus) {
            val router = RouterVo()
            router.hidden = "1" == menu.visible
            router.name = menu.routeName
            router.path = menu.routerPath
            router.component = menu.componentInfo
            router.query = menu.queryParam
            router.meta = MetaVo(menu.menuName!!, menu.icon!!, StringUtils.equals("1", menu.isCache), menu.path)
            val cMenus = menu.children
            if (CollUtil.isNotEmpty(cMenus) && UserConstants.TYPE_DIR == menu.menuType) {
                router.alwaysShow = true
                router.redirect = "noRedirect"
                router.children = buildMenus(cMenus)
            } else if (menu.isMenuFrame) {
                router.meta = null
                val childrenList: MutableList<RouterVo> = ArrayList()
                val children = RouterVo()
                children.path = menu.path
                children.component = menu.component
                children.name = StringUtils.capitalize(menu.path)
                children.meta = MetaVo(menu.menuName!!, menu.icon!!, StringUtils.equals("1", menu.isCache), menu.path)
                children.query = menu.queryParam
                childrenList.add(children)
                router.children = childrenList
            } else if (menu.parentId == 0.toLong() && menu.isInnerLink) {
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
    override fun buildMenuTreeSelect(menus: MutableList<SysMenuVo>): MutableList<Tree<Long>>? {
        return if (CollUtil.isEmpty(menus)) {
            CollUtil.newArrayList()
        } else build(menus) { menu: SysMenuVo, tree: Tree<Long> ->
            tree.setId(menu.menuId)
                .setParentId(menu.parentId)
                .setName(menu.menuName)
                .setWeight(menu.orderNum)
        }?.toMutableList()
    }

    /**
     * 根据菜单ID查询信息
     *
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    override fun selectMenuById(menuId: Long): SysMenuVo? {
        return baseMapper.selectVoById(menuId)!!
    }

    /**
     * 是否存在菜单子节点
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    override fun hasChildByMenuId(menuId: Long): Boolean {
        /*return baseMapper.exists(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getParentId, menuId));*/
        return false
    }

    /**
     * 查询菜单使用数量
     *
     * @param menuId 菜单ID
     * @return 结果
     */
    override fun checkMenuExistRole(menuId: Long): Boolean {
        /*return roleMenuMapper.exists(new LambdaQueryWrapper<SysRoleMenu>().eq(SysRoleMenu::getMenuId, menuId));*/
        return false
    }

    /**
     * 新增保存菜单信息
     *
     * @param bo 菜单信息
     * @return 结果
     */
    override fun insertMenu(bo: SysMenuBo): Int {
        val menu = convert(bo, SysMenu::class.java)!!
        return baseMapper.insert(menu)
    }

    /**
     * 修改保存菜单信息
     *
     * @param bo 菜单信息
     * @return 结果
     */
    override fun updateMenu(bo: SysMenuBo): Int {
        /*SysMenu menu = MapstructUtils.convert(bo, SysMenu.class);
        return baseMapper.updateById(menu);*/
        return 0
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
        /*boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysMenu>()
            .eq(SysMenu::getMenuName, menu.getMenuName())
            .eq(SysMenu::getParentId, menu.getParentId())
            .ne(ObjectUtil.isNotNull(menu.getMenuId()), SysMenu::getMenuId, menu.getMenuId()));
        return !exist;*/
        return false
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
        val childList = filter(list, Predicate { n: SysMenu -> n.parentId == t.menuId })
        t.children = childList
        for (tChild in childList) {
            // 判断是否有子节点
            if (list.stream().anyMatch { n: SysMenu -> n.parentId == tChild.menuId }) {
                recursionFn(list, tChild)
            }
        }
    }
}
