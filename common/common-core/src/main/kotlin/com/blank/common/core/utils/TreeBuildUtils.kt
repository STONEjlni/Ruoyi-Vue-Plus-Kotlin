package com.blank.common.core.utils

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.lang.tree.Tree
import cn.hutool.core.lang.tree.TreeNodeConfig
import cn.hutool.core.lang.tree.TreeUtil
import cn.hutool.core.lang.tree.parser.NodeParser
import com.blank.common.core.utils.reflect.ReflectUtils

/**
 * 扩展 hutool TreeUtil 封装系统树构建
 */
object TreeBuildUtils {
    /**
     * 根据前端定制差异化字段
     */
    private val DEFAULT_CONFIG = TreeNodeConfig.DEFAULT_CONFIG.setNameKey("label")

    fun <T, K> build(list: List<T>, nodeParser: NodeParser<T, K>): List<Tree<K>>? {
        if (CollUtil.isEmpty(list)) {
            return null
        }
        val k = ReflectUtils.invokeGetter<K>(list[0] as Any, "parentId")
        return TreeUtil.build(list, k, DEFAULT_CONFIG, nodeParser)
    }
}
