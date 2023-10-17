package com.blank.common.core.constant

/**
 * 用户常量信息
 */
interface UserConstants {
    companion object {
        /**
         * 平台内系统用户的唯一标志
         */
        const val SYS_USER = "SYS_USER"

        /**
         * 正常状态
         */
        const val NORMAL = "0"

        /**
         * 异常状态
         */
        const val EXCEPTION = "1"

        /**
         * 用户正常状态
         */
        const val USER_NORMAL = "0"

        /**
         * 用户封禁状态
         */
        const val USER_DISABLE = "1"

        /**
         * 角色正常状态
         */
        const val ROLE_NORMAL = "0"

        /**
         * 角色封禁状态
         */
        const val ROLE_DISABLE = "1"

        /**
         * 部门正常状态
         */
        const val DEPT_NORMAL = "0"

        /**
         * 部门停用状态
         */
        const val DEPT_DISABLE = "1"

        /**
         * 岗位正常状态
         */
        const val POST_NORMAL = "0"

        /**
         * 岗位停用状态
         */
        const val POST_DISABLE = "1"

        /**
         * 字典正常状态
         */
        const val DICT_NORMAL = "0"

        /**
         * 是否为系统默认（是）
         */
        const val YES = "Y"

        /**
         * 是否菜单外链（是）
         */
        const val YES_FRAME = "0"

        /**
         * 是否菜单外链（否）
         */
        const val NO_FRAME = "1"

        /**
         * 菜单正常状态
         */
        const val MENU_NORMAL = "0"

        /**
         * 菜单停用状态
         */
        const val MENU_DISABLE = "1"

        /**
         * 菜单类型（目录）
         */
        const val TYPE_DIR = "M"

        /**
         * 菜单类型（菜单）
         */
        const val TYPE_MENU = "C"

        /**
         * 菜单类型（按钮）
         */
        const val TYPE_BUTTON = "F"

        /**
         * Layout组件标识
         */
        const val LAYOUT = "Layout"

        /**
         * ParentView组件标识
         */
        const val PARENT_VIEW = "ParentView"

        /**
         * InnerLink组件标识
         */
        const val INNER_LINK = "InnerLink"

        /**
         * 用户名长度限制
         */
        const val USERNAME_MIN_LENGTH = 2
        const val USERNAME_MAX_LENGTH = 20

        /**
         * 密码长度限制
         */
        const val PASSWORD_MIN_LENGTH = 5
        const val PASSWORD_MAX_LENGTH = 20

        /**
         * 超级管理员ID
         */
        const val SUPER_ADMIN_ID: Long = 1L

        /**
         * 超级管理员角色 roleKey
         */
        const val SUPER_ADMIN_ROLE_KEY = "superadmin"

    }
}
