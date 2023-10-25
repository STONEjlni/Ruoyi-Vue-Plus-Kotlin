package com.blank.system.domain.bo

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.system.domain.SysMenu
import com.fasterxml.jackson.annotation.JsonInclude
import io.github.linpeilie.annotations.AutoMapper
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

/**
 * 菜单权限业务对象 sys_menu
 */
@AutoMapper(target = SysMenu::class, reverseConvertGenerate = false)
class SysMenuBo : BaseEntity() {
    /**
     * 菜单ID
     */
    var menuId: @NotNull(message = "菜单ID不能为空") Long? = null

    /**
     * 父菜单ID
     */
    var parentId: Long? = null

    /**
     * 菜单名称
     */
    var menuName: @NotBlank(message = "菜单名称不能为空") @Size(
        min = 0,
        max = 50,
        message = "菜单名称长度不能超过{max}个字符"
    ) String? = null

    /**
     * 显示顺序
     */
    var orderNum: @NotNull(message = "显示顺序不能为空") Int? =
        null

    /**
     * 路由地址
     */
    var path: @Size(min = 0, max = 200, message = "路由地址不能超过{max}个字符") String? = null

    /**
     * 组件路径
     */
    var component: @Size(min = 0, max = 200, message = "组件路径不能超过{max}个字符") String? = null

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
    var menuType: @NotBlank(
        message = "菜单类型不能为空"
    ) String? = null

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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var perms: @Size(min = 0, max = 100, message = "权限标识长度不能超过{max}个字符") String? = null

    /**
     * 菜单图标
     */
    var icon: String? = null

    /**
     * 备注
     */
    var remark: String? = null
}
