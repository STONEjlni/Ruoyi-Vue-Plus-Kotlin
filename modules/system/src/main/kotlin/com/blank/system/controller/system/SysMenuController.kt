package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.dev33.satoken.annotation.SaCheckRole
import cn.dev33.satoken.annotation.SaMode
import cn.hutool.core.lang.tree.Tree
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.domain.R.Companion.warn
import com.blank.common.core.utils.StringUtilsExtend.ishttp
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.satoken.utils.LoginHelper.getUserId
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysMenuBo
import com.blank.system.domain.vo.MenuTreeSelectVo
import com.blank.system.domain.vo.RouterVo
import com.blank.system.domain.vo.SysMenuVo
import com.blank.system.service.ISysMenuService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 菜单信息
 */
@Validated
@RestController
@RequestMapping("/system/menu")
class SysMenuController(
    private val menuService: ISysMenuService
) : BaseController() {

    /**
     * 获取路由信息
     *
     * @return 路由信息
     */
    @GetMapping("/getRouters")
    fun getRouters(): R<MutableList<RouterVo>> {
        val menus = menuService.selectMenuTreeByUserId(getUserId()!!)!!
        return ok(data = menuService.buildMenus(menus))
    }

    /**
     * 获取菜单列表
     */
    @SaCheckRole(value = [UserConstants.SUPER_ADMIN_ROLE_KEY], mode = SaMode.OR)
    @SaCheckPermission("system:menu:list")
    @GetMapping("/list")
    fun list(menu: SysMenuBo): R<MutableList<SysMenuVo>> {
        val menus = menuService.selectMenuList(menu, getUserId()!!)
        return ok(data = menus)
    }

    /**
     * 根据菜单编号获取详细信息
     *
     * @param menuId 菜单ID
     */
    @SaCheckRole(value = [UserConstants.SUPER_ADMIN_ROLE_KEY], mode = SaMode.OR)
    @SaCheckPermission("system:menu:query")
    @GetMapping(value = ["/{menuId}"])
    fun getInfo(@PathVariable menuId: Long): R<SysMenuVo> {
        return ok(data = menuService.selectMenuById(menuId))
    }

    /**
     * 获取菜单下拉树列表
     */
    @SaCheckPermission("system:menu:query")
    @GetMapping("/treeselect")
    fun treeselect(menu: SysMenuBo): R<MutableList<Tree<Long>>> {
        val menus = menuService.selectMenuList(menu, getUserId()!!)!!
        return ok(data = menuService.buildMenuTreeSelect(menus))
    }

    /**
     * 加载对应角色菜单列表树
     *
     * @param roleId 角色ID
     */
    @SaCheckPermission("system:menu:query")
    @GetMapping(value = ["/roleMenuTreeselect/{roleId}"])
    fun roleMenuTreeselect(@PathVariable("roleId") roleId: Long): R<MenuTreeSelectVo> {
        val menus = menuService.selectMenuList(getUserId()!!)!!
        val selectVo = MenuTreeSelectVo()
        selectVo.checkedKeys = menuService.selectMenuListByRoleId(roleId)
        selectVo.menus = menuService.buildMenuTreeSelect(menus)
        return ok(data = selectVo)
    }

    /**
     * 加载对应租户套餐菜单列表树
     *
     * @param packageId 租户套餐ID
     */
    @SaCheckRole(UserConstants.SUPER_ADMIN_ROLE_KEY)
    @SaCheckPermission("system:menu:query")
    @GetMapping(value = ["/tenantPackageMenuTreeselect/{packageId}"])
    fun tenantPackageMenuTreeselect(@PathVariable("packageId") packageId: Long): R<MenuTreeSelectVo> {
        val menus = menuService.selectMenuList(getUserId()!!)!!
        val selectVo = MenuTreeSelectVo()
        selectVo.checkedKeys = menuService.selectMenuListByPackageId(packageId)
        selectVo.menus = menuService.buildMenuTreeSelect(menus)
        return ok(data = selectVo)
    }

    /**
     * 新增菜单
     */
    @SaCheckRole(UserConstants.SUPER_ADMIN_ROLE_KEY)
    @SaCheckPermission("system:menu:add")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody menu: SysMenuBo): R<Unit> {
        if (!menuService.checkMenuNameUnique(menu)) {
            return fail(msg = "新增菜单'${menu.menuName}'失败，菜单名称已存在")
        } else if (UserConstants.YES_FRAME == menu.isFrame && !ishttp(menu.path)) {
            return fail(msg = "新增菜单'${menu.menuName}'失败，地址必须以http(s)://开头")
        }
        return toAjax(menuService.insertMenu(menu))
    }

    /**
     * 修改菜单
     */
    @SaCheckRole(UserConstants.SUPER_ADMIN_ROLE_KEY)
    @SaCheckPermission("system:menu:edit")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody menu: SysMenuBo): R<Unit> {
        if (!menuService.checkMenuNameUnique(menu)) {
            return fail(msg = "修改菜单'${menu.menuName}'失败，菜单名称已存在")
        } else if (UserConstants.YES_FRAME == menu.isFrame && !ishttp(menu.path)) {
            return fail(msg = "修改菜单'${menu.menuName}'失败，地址必须以http(s)://开头")
        } else if (menu.menuId == menu.parentId) {
            return fail(msg = "修改菜单'${menu.menuName}'失败，上级菜单不能选择自己")
        }
        return toAjax(menuService.updateMenu(menu))
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    @SaCheckRole(UserConstants.SUPER_ADMIN_ROLE_KEY)
    @SaCheckPermission("system:menu:remove")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    fun remove(@PathVariable("menuId") menuId: Long): R<Unit> {
        if (menuService.hasChildByMenuId(menuId)) {
            return warn(msg = "存在子菜单,不允许删除")
        }
        return if (menuService.checkMenuExistRole(menuId)) {
            warn(msg = "菜单已分配,不允许删除")
        } else toAjax(menuService.deleteMenuById(menuId))
    }
}
