package com.blank.web.service

import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.bean.BeanUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.LoginType
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.constant.Constants
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.dto.RoleDTO
import com.blank.common.core.domain.model.LoginUser
import com.blank.common.core.exception.user.UserException
import com.blank.common.core.utils.DateUtils.getNowDate
import com.blank.common.core.utils.JakartaServletUtilExtend.getClientIP
import com.blank.common.core.utils.JakartaServletUtilExtend.getRequest
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.core.utils.SpringUtilExtend.context
import com.blank.common.log.event.LogininforEvent
import com.blank.common.redis.utils.RedisUtils.deleteObject
import com.blank.common.redis.utils.RedisUtils.getCacheObject
import com.blank.common.redis.utils.RedisUtils.setCacheObject
import com.blank.common.satoken.utils.LoginHelper.getLoginUser
import com.blank.common.satoken.utils.LoginHelper.getUserId
import com.blank.system.domain.SysUser
import com.blank.system.domain.bo.SysSocialBo
import com.blank.system.domain.vo.SysUserVo
import com.blank.system.mapper.SysUserMapper
import com.blank.system.service.ISysPermissionService
import com.blank.system.service.ISysSocialService
import me.zhyd.oauth.model.AuthUser
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.function.Supplier

/**
 * 登录校验方法
 */
@Slf4j
@Service
class SysLoginService(
    private val permissionService: ISysPermissionService,
    private val sysSocialService: ISysSocialService,
    private val userMapper: SysUserMapper
) {
    @Value("\${user.password.maxRetryCount}")
    private val maxRetryCount: Int = 5

    @Value("\${user.password.lockTime}")
    private val lockTime: Int = 10


    /**
     * 绑定第三方用户
     *
     * @param authUserData 授权响应实体
     * @return 统一响应实体
     */
    fun socialRegister(authUserData: AuthUser) {
        val authId = authUserData.source + authUserData.uuid
        // 第三方用户信息
        val bo = BeanUtil.toBean(authUserData, SysSocialBo::class.java)
        BeanUtil.copyProperties(authUserData.token, bo)
        bo.userId = getUserId()
        bo.authId = authId
        bo.openId = authUserData.uuid
        bo.userName = authUserData.username
        bo.nickName = authUserData.nickname
        // 查询是否已经绑定用户
        val vo = sysSocialService.selectByAuthId(authId)
        if (ObjectUtil.isEmpty(vo)) {
            // 没有绑定用户, 新增用户信息
            sysSocialService.insertByBo(bo)
        } else {
            // 更新用户信息
            bo.id = vo!!.id
            sysSocialService.updateByBo(bo)
        }
    }

    /**
     * 退出登录
     */
    fun logout() {
        try {
            val loginUser = getLoginUser()
            recordLogininfor(loginUser!!.username!!, Constants.LOGOUT, message("user.logout.success"))
        } catch (ignored: NotLoginException) {
        } finally {
            try {
                StpUtil.logout()
            } catch (ignored: NotLoginException) {
            }
        }
    }

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     */
    fun recordLogininfor(username: String, status: String, message: String) {
        val logininforEvent = LogininforEvent()
        logininforEvent.username = username
        logininforEvent.status = status
        logininforEvent.message = message
        logininforEvent.request = getRequest()
        context().publishEvent(logininforEvent)
    }

    /**
     * 构建登录用户
     */
    fun buildLoginUser(user: SysUserVo): LoginUser {
        val loginUser = LoginUser()
        loginUser.userId = user.userId
        loginUser.deptId = user.deptId
        loginUser.username = user.userName
        loginUser.nickname = user.nickName
        loginUser.userType = user.userType
        loginUser.menuPermission = permissionService.getMenuPermission(user.userId!!)!!
        loginUser.rolePermission = permissionService.getRolePermission(user.userId!!)!!
        loginUser.deptName = if (ObjectUtil.isNull(user.dept)) "" else user.dept!!.deptName
        val roles = BeanUtil.copyToList(user.roles, RoleDTO::class.java)
        loginUser.roles = roles
        return loginUser
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    fun recordLoginInfo(userId: Long) {
        val sysUser = SysUser()
        sysUser.userId = userId
        sysUser.loginIp = getClientIP()
        sysUser.loginDate = getNowDate()
        sysUser.updateBy = userId
        userMapper.update(sysUser)
        //        userMapper.updateById(sysUser);
    }

    /**
     * 登录校验
     */
    fun checkLogin(loginType: LoginType, username: String, supplier: Supplier<Boolean>) {
        val errorKey = GlobalConstants.PWD_ERR_CNT_KEY + username
        val loginFail = Constants.LOGIN_FAIL

        // 获取用户登录错误次数，默认为0 (可自定义限制策略 例如: key + username + ip)
        var errorNumber = ObjectUtil.defaultIfNull(getCacheObject(errorKey), 0)
        // 锁定时间内登录 则踢出
        if (errorNumber >= maxRetryCount) {
            recordLogininfor(username, loginFail, message(loginType.retryLimitExceed, maxRetryCount, lockTime))
            throw UserException(loginType.retryLimitExceed, maxRetryCount.toString(), lockTime)
        }
        if (supplier.get()) {
            // 错误次数递增
            errorNumber++
            setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime.toLong()))
            // 达到规定错误次数 则锁定登录
            if (errorNumber >= maxRetryCount) {
                recordLogininfor(username, loginFail, message(loginType.retryLimitExceed, maxRetryCount, lockTime))
                throw UserException(loginType.retryLimitExceed, maxRetryCount.toString(), lockTime)
            } else {
                // 未达到规定错误次数
                recordLogininfor(username, loginFail, message(loginType.retryLimitCount, errorNumber))
                throw UserException(loginType.retryLimitCount, errorNumber.toString())
            }
        }

        // 登录成功 清空错误次数
        deleteObject(errorKey)
    }
}
