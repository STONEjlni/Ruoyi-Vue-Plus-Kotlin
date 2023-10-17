package com.blank.common.satoken.utils

import cn.dev33.satoken.context.SaHolder
import cn.dev33.satoken.stp.SaLoginModel
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.convert.Convert
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.annotation.UserType
import com.blank.common.core.annotation.UserType.Companion.getUserType
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.domain.model.LoginUser


/**
 * 登录鉴权助手
 * <p>
 * user_type 为 用户类型 同一个用户表 可以有多种用户类型 例如 pc,app
 * deivce 为 设备类型 同一个用户类型 可以有 多种设备类型 例如 web,ios
 * 可以组成 用户类型与设备类型多对多的 权限灵活控制
 * <p>
 * 多用户体系 针对 多种用户类型 但权限控制不一致
 * 可以组成 多用户类型表与多设备类型 分别控制权限
 */
@Slf4j
object LoginHelper {
    const val LOGIN_USER_KEY = "loginUser"
    const val USER_KEY = "userId"
    const val DEPT_KEY = "deptId"
    const val CLIENT_KEY = "clientid"

    /**
     * 登录系统 基于 设备类型
     * 针对相同用户体系不同设备
     *
     * @param loginUser 登录用户信息
     * @param model     配置参数
     */
    @JvmStatic
    fun login(loginUser: LoginUser, model: SaLoginModel) {
        var model = model
        val storage = SaHolder.getStorage()
        storage[LOGIN_USER_KEY] = loginUser
        storage[USER_KEY] = loginUser.userId
        storage[DEPT_KEY] = loginUser.deptId
        model = ObjectUtil.defaultIfNull(model, SaLoginModel())
        StpUtil.login(
            loginUser.getLoginId(),
            model.setExtra(USER_KEY, loginUser.userId)
                .setExtra(DEPT_KEY, loginUser.deptId)
        )
        StpUtil.getSession()[LOGIN_USER_KEY] = loginUser
    }

    /**
     * 获取用户(多级缓存)
     */
    @JvmStatic
    fun getLoginUser(): LoginUser? {
        var loginUser = SaHolder.getStorage()[LOGIN_USER_KEY] as LoginUser?
        if (loginUser != null) {
            return loginUser
        }
        val session = StpUtil.getSession()
        if (ObjectUtil.isNull(session)) {
            return null
        }
        loginUser = session[LOGIN_USER_KEY] as LoginUser
        SaHolder.getStorage()[LOGIN_USER_KEY] = loginUser
        return loginUser
    }

    /**
     * 获取用户基于token
     */
    @JvmStatic
    fun getLoginUser(token: String?): LoginUser? {
        val loginId = StpUtil.getLoginIdByToken(token)
        val session = StpUtil.getSessionByLoginId(loginId)
        return if (ObjectUtil.isNull(session)) {
            null
        } else session[LOGIN_USER_KEY] as LoginUser?
    }

    /**
     * 获取用户id
     */
    @JvmStatic
    fun getUserId(): Long? {
        return Convert.toLong(getExtra(USER_KEY))
    }

    /**
     * 获取部门ID
     */
    @JvmStatic
    fun getDeptId(): Long? {
        return Convert.toLong(getExtra(DEPT_KEY))
    }

    private fun getExtra(key: String): Any? {
        var obj: Any?
        try {
            obj = SaHolder.getStorage()[key]
            if (ObjectUtil.isNull(obj)) {
                obj = StpUtil.getExtra(key)
                SaHolder.getStorage()[key] = obj
            }
        } catch (e: java.lang.Exception) {
            return null
        }
        return obj
    }

    /**
     * 获取用户账户
     */
    @JvmStatic
    fun getUsername(): String? {
        return getLoginUser()!!.username
    }

    /**
     * 获取用户类型
     */
    @JvmStatic
    fun getUserType(): UserType {
        val loginType = StpUtil.getLoginIdAsString()
        return getUserType(loginType)
    }

    /**
     * 是否为超级管理员
     *
     * @param userId 用户ID
     * @return 结果
     */
    @JvmStatic
    fun isSuperAdmin(userId: Long): Boolean {
        return UserConstants.SUPER_ADMIN_ID == userId
    }

    @JvmStatic
    fun isSuperAdmin(): Boolean {
        return isSuperAdmin(getUserId()!!)
    }

    /**
     * 获取登录用户名
     */
    @JvmStatic
    fun getLoginUserId(): Long? {
        val loginUser: LoginUser? = try {
            getLoginUser()
        } catch (e: Exception) {
            log.warn { "自动注入警告 => 用户未登录" }
            return null
        }
        return if (ObjectUtil.isNotNull(loginUser)) loginUser!!.userId else null
    }
}
