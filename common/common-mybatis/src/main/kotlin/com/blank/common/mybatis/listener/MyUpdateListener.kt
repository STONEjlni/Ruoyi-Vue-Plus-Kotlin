package com.blank.common.mybatis.listener

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.common.satoken.utils.LoginHelper.getLoginUserId
import com.mybatisflex.annotation.UpdateListener
import java.util.*

/**
 * 修改监听器
 */
class MyUpdateListener : UpdateListener {
    override fun onUpdate(entity: Any) {
        if (entity is BaseEntity) {
            //设置被新增时的一些默认数据
            entity.updateBy = getLoginUserId()
            entity.updateTime = Date()
        }
    }
}
