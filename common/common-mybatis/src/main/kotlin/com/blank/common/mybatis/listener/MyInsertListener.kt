package com.blank.common.mybatis.listener

import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.common.satoken.utils.LoginHelper.getLoginUserId
import com.mybatisflex.annotation.InsertListener
import java.util.*

/**
 * 插入监听器
 */
class MyInsertListener : InsertListener {
    override fun onInsert(entity: Any) {
        if (entity is BaseEntity) {
            //设置被新增时的一些默认数据
            entity.createBy = getLoginUserId()
            entity.createTime = Date()
        }
    }
}
