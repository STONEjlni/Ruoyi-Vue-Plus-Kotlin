package com.blank.system.domain

import com.blank.common.core.constant.Constants
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.utils.StringUtilsExtend.ishttp
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import org.apache.commons.lang3.StringUtils

/**
 * 菜单权限表 sys_menu
 */
@Table("sys_menu")
class SysMenu : BaseEntity() {
    /**
     * 菜单ID
     */
    @Id
    var menuId: Long? = null

    /**
     * 父菜单ID
     */
    var parentId: Long? = null

    /**
     * 菜单名称
     */
    var menuName: String? = null

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
     * 类型（M目录 C菜单 F按钮）
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
     * 权限字符串
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
     * 父菜单名称
     */
    @Column(ignore = true)
    var parentName: String? = null

    /**
     * 子菜单
     */
    @Column(ignore = true)
    var children: MutableList<SysMenu> = mutableListOf()
    val routeName: String
        /**
         * 获取路由名称
         */
        get() {
            var routerName = StringUtils.capitalize(path)
            // 非外链并且是一级目录（类型为目录）
            if (isMenuFrame) {
                routerName = StringUtils.EMPTY
            }
            return routerName
        }
    val routerPath: String?
        /**
         * 获取路由地址
         */
        get() {
            var routerPath = path
            // 内链打开外网方式
            if (parentId != 0L && isInnerLink) {
                routerPath = innerLinkReplaceEach(routerPath)
            }
            // 非外链并且是一级目录（类型为目录）
            if (0L == parentId && UserConstants.TYPE_DIR == menuType && UserConstants.NO_FRAME == isFrame) {
                routerPath = "/$path"
            } else if (isMenuFrame) {
                routerPath = "/"
            }
            return routerPath
        }
    val componentInfo: String?
        /**
         * 获取组件信息
         */
        get() {
            var component: String? = UserConstants.LAYOUT
            if (StringUtils.isNotEmpty(this.component) && !isMenuFrame) {
                component = this.component
            } else if (StringUtils.isEmpty(this.component) && parentId != 0L && isInnerLink) {
                component = UserConstants.INNER_LINK
            } else if (StringUtils.isEmpty(this.component) && isParentView) {
                component = UserConstants.PARENT_VIEW
            }
            return component
        }
    val isMenuFrame: Boolean
        /**
         * 是否为菜单内部跳转
         */
        get() = parentId == 0L && UserConstants.TYPE_MENU == menuType && isFrame == UserConstants.NO_FRAME
    val isInnerLink: Boolean
        /**
         * 是否为内链组件
         */
        get() = isFrame == UserConstants.NO_FRAME && ishttp(path)
    val isParentView: Boolean
        /**
         * 是否为parent_view组件
         */
        get() = parentId != 0L && UserConstants.TYPE_DIR == menuType

    companion object {
        /**
         * 内链域名特殊字符替换
         */
        fun innerLinkReplaceEach(path: String?): String {
            return StringUtils.replaceEach(
                path,
                arrayOf(Constants.HTTP, Constants.HTTPS, Constants.WWW, "."),
                arrayOf("", "", "", "/")
            )
        }
    }
}
