package com.blank.system.domain.vo

import com.fasterxml.jackson.annotation.JsonInclude

/**
 * 路由配置信息
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
class RouterVo {
    /**
     * 路由名字
     */
    var name: String? = null

    /**
     * 路由地址
     */
    var path: String? = null

    /**
     * 是否隐藏路由，当设置 true 的时候该路由不会再侧边栏出现
     */
    var hidden = false

    /**
     * 重定向地址，当设置 noRedirect 的时候该路由在面包屑导航中不可被点击
     */
    var redirect: String? = null

    /**
     * 组件地址
     */
    var component: String? = null

    /**
     * 路由参数：如 {"id": 1, "name": "ry"}
     */
    var query: String? = null

    /**
     * 当你一个路由下面的 children 声明的路由大于1个时，自动会变成嵌套的模式--如组件页面
     */
    var alwaysShow: Boolean? = null

    /**
     * 其他元素
     */
    var meta: MetaVo? = null

    /**
     * 子路由
     */
    var children: MutableList<RouterVo>? = mutableListOf()
}
