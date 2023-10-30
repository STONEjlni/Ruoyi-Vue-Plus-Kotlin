package com.blank.system.controller.monitor

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.CacheConstants
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.domain.dto.UserOnlineDTO
import com.blank.common.core.utils.StreamUtils.filter
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.mybatis.core.page.TableDataInfo.Companion.build
import com.blank.common.redis.utils.RedisUtils.getCacheObject
import com.blank.common.web.core.BaseController
import com.blank.system.domain.SysUserOnline
import org.apache.commons.lang3.StringUtils
import org.springframework.web.bind.annotation.*

/**
 * 在线用户监控
 */
@RestController
@RequestMapping("/monitor/online")
class SysUserOnlineController : BaseController() {
    /**
     * 获取在线用户监控列表
     *
     * @param ipaddr   IP地址
     * @param userName 用户名
     */
    @SaCheckPermission("monitor:online:list")
    @GetMapping("/list")
    fun list(ipaddr: String?, userName: String?): TableDataInfo<SysUserOnline> {
        // 获取所有未过期的 token
        val keys = StpUtil.searchTokenValue("", 0, -1, false)
        var userOnlineDTOList: MutableList<UserOnlineDTO> = mutableListOf()
        for (key in keys) {
            val token = StringUtils.substringAfterLast(key, ":")
            // 如果已经过期则跳过
            if (StpUtil.stpLogic.getTokenActiveTimeoutByToken(token) < -1) {
                continue
            }
            val cacheObject = getCacheObject<UserOnlineDTO>(CacheConstants.ONLINE_TOKEN_KEY + token)
            if (ObjectUtil.isNotNull(cacheObject)) {
                userOnlineDTOList.add(cacheObject!!)
            }
        }
        if (StringUtils.isNotEmpty(ipaddr) && StringUtils.isNotEmpty(userName)) {
            userOnlineDTOList = filter(
                userOnlineDTOList
            ) { userOnline: UserOnlineDTO? ->
                StringUtils.equals(ipaddr, userOnline!!.ipaddr) &&
                    StringUtils.equals(userName, userOnline.userName)
            }
        } else if (StringUtils.isNotEmpty(ipaddr)) {
            userOnlineDTOList = filter(
                userOnlineDTOList
            ) { userOnline: UserOnlineDTO? -> StringUtils.equals(ipaddr, userOnline!!.ipaddr) }
        } else if (StringUtils.isNotEmpty(userName)) {
            userOnlineDTOList = filter(
                userOnlineDTOList
            ) { userOnline: UserOnlineDTO? -> StringUtils.equals(userName, userOnline!!.userName) }
        }
        userOnlineDTOList.reverse()
        userOnlineDTOList.removeAll(setOf<Any?>(null))
        val userOnlineList = BeanUtil.copyToList(userOnlineDTOList, SysUserOnline::class.java)
        return build(userOnlineList)
    }

    /**
     * 强退用户
     *
     * @param tokenId token值
     */
    @SaCheckPermission("monitor:online:forceLogout")
    @Log(title = "在线用户", businessType = BusinessType.FORCE)
    @DeleteMapping("/{tokenId}")
    fun forceLogout(@PathVariable tokenId: String?): R<Unit> {
        try {
            StpUtil.kickoutByTokenValue(tokenId)
        } catch (ignored: NotLoginException) {
        }
        return ok()
    }
}
