package com.blank.system.service.impl

import cn.dev33.satoken.stp.StpUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.satoken.utils.LoginHelper.isSuperAdmin
import com.blank.common.sensitive.core.SensitiveService
import org.springframework.stereotype.Service


/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 */
@Service
class SysSensitiveServiceImpl : SensitiveService {
    /**
     * 是否脱敏
     */
    override fun isSensitive(roleKey: String?, perms: String?): Boolean {
        if (!StpUtil.isLogin()) {
            return true
        }
        val roleExist: Boolean = StrUtil.isNotBlank(roleKey)
        val permsExist: Boolean = StrUtil.isNotBlank(perms)
        if (roleExist && permsExist) {
            if (StpUtil.hasRole(roleKey) && StpUtil.hasPermission(perms)) {
                return false
            }
        } else if (roleExist && StpUtil.hasRole(roleKey)) {
            return false
        } else if (permsExist && StpUtil.hasPermission(perms)) {
            return false
        }

        return !isSuperAdmin()
    }
}
