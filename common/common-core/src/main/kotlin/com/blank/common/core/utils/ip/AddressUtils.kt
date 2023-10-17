package com.blank.common.core.utils.ip

import cn.hutool.core.net.NetUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.http.HtmlUtil
import com.blank.common.core.annotation.Slf4j

/**
 * 获取地址类
 */
@Slf4j
object AddressUtils {
    // 未知地址
    const val UNKNOWN = "XX XX"

    @JvmStatic
    fun getRealAddressByIP(ip: String): String {
        var ip = ip
        if (StrUtil.isBlank(ip)) {
            return UNKNOWN
        }
        // 内网不查询
        ip = if ("0:0:0:0:0:0:0:1" == ip) "127.0.0.1" else HtmlUtil.cleanHtmlTag(ip)
        return if (NetUtil.isInnerIP(ip)) {
            "内网IP"
        } else RegionUtils.getCityInfo(ip)
    }
}
