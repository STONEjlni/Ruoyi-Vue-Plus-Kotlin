package com.blank.system.domain.vo

import com.blank.common.core.utils.StringUtilsExtend.ishttp

/**
 * 路由显示信息
 */
class MetaVo {
    /**
     * 设置该路由在侧边栏和面包屑中展示的名字
     */
    var title: String

    /**
     * 设置该路由的图标，对应路径src/assets/icons/svg
     */
    var icon: String

    /**
     * 设置为true，则不会被 <keep-alive>缓存
    </keep-alive> */
    var noCache = false

    /**
     * 内链地址（http(s)://开头）
     */
    var link: String? = null

    constructor(title: String, icon: String) {
        this.title = title
        this.icon = icon
    }

    constructor(title: String, icon: String, noCache: Boolean) {
        this.title = title
        this.icon = icon
        this.noCache = noCache
    }

    constructor(title: String, icon: String, link: String?) {
        this.title = title
        this.icon = icon
        this.link = link
    }

    constructor(title: String, icon: String, noCache: Boolean, link: String?) {
        this.title = title
        this.icon = icon
        this.noCache = noCache
        if (ishttp(link)) {
            this.link = link
        }
    }
}
