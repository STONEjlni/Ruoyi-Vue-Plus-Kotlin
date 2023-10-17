package com.blank.system.domain.vo

import cn.hutool.core.lang.tree.Tree

/**
 * 角色菜单列表树信息
 */
class MenuTreeSelectVo {
    /**
     * 选中菜单列表
     */
    var checkedKeys: MutableList<Long>? = null

    /**
     * 菜单下拉树结构列表
     */
    var menus: MutableList<Tree<Long>>? = null
}
