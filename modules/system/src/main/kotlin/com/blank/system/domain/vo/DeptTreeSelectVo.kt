package com.blank.system.domain.vo

import cn.hutool.core.lang.tree.Tree

/**
 * 角色部门列表树信息
 */
class DeptTreeSelectVo {
    /**
     * 选中部门列表
     */
    var checkedKeys: MutableList<Long>? = null

    /**
     * 下拉树结构列表
     */
    var depts: MutableList<Tree<Long>>? = null
}
