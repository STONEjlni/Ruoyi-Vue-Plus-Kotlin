package com.blank.system.service.impl

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
    override fun isSensitive(): Boolean {
        return !isSuperAdmin()
    }
}
