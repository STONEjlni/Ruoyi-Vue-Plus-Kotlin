package com.blank.system.domain.vo

import com.blank.system.domain.SysMenu
import io.github.linpeilie.annotations.AutoMapper
import java.io.Serial
import java.io.Serializable
import java.util.*

/**
 * 菜单权限视图对象 sys_menu
 */
@AutoMapper(target = SysMenu::class)
class SysMenuVo : Serializable {
    /**
     * 菜单ID
     */
    var menuId: Long? = null

    /**
     * 菜单名称
     */
    var menuName: String? = null

    /**
     * 父菜单ID
     */
    var parentId: Long? = null

    /**
     * 显示顺序
     */
    var orderNum: Int? = null

    /**
     * 路由地址
     */
    var path: String? = null

    /**
     * 组件路径
     */
    var component: String? = null

    /**
     * 路由参数
     */
    var queryParam: String? = null

    /**
     * 是否为外链（0是 1否）
     */
    var isFrame: String? = null

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    var isCache: String? = null

    /**
     * 菜单类型（M目录 C菜单 F按钮）
     */
    var menuType: String? = null

    /**
     * 显示状态（0显示 1隐藏）
     */
    var visible: String? = null

    /**
     * 菜单状态（0正常 1停用）
     */
    var status: String? = null

    /**
     * 权限标识
     */
    var perms: String? = null

    /**
     * 菜单图标
     */
    var icon: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 创建时间
     */
    var createTime: Date? = null

    /**
     * 子菜单
     */
    var children: MutableList<SysMenuVo> = ArrayList()

    companion object {
        @Serial
        private const val serialVersionUID = 1L
    }
}
