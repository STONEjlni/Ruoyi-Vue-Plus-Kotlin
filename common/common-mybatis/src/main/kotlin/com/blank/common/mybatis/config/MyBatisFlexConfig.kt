package com.blank.common.mybatis.config

import cn.hutool.core.util.ObjectUtil
import cn.hutool.http.HttpStatus
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.domain.model.LoginUser
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.factory.YmlPropertySourceFactory
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.common.satoken.utils.LoginHelper.getLoginUser
import com.mybatisflex.core.FlexGlobalConfig
import com.mybatisflex.core.audit.AuditManager
import com.mybatisflex.core.audit.AuditMessage
import com.mybatisflex.core.query.QueryColumnBehavior
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.PropertySource
import org.springframework.transaction.annotation.EnableTransactionManagement
import java.util.*

/**
 * mybatis-flex配置类
 */
@EnableTransactionManagement(proxyTargetClass = true)
@AutoConfiguration
@MapperScan("\${mybatis-flex.mapperPackage}")
@PropertySource(value = ["classpath:common-mybatis.yml"], factory = YmlPropertySourceFactory::class)
@Slf4j
class MyBatisFlexConfig : MyBatisFlexCustomizer {

    companion object {
        init {
            QueryColumnBehavior.setIgnoreFunction(QueryColumnBehavior.IGNORE_BLANK)
            QueryColumnBehavior.setSmartConvertInToEquals(true)
        }
    }

    override fun customize(globalConfig: FlexGlobalConfig) {
        //我们可以在这里进行一些列的初始化配置
        AuditManager.setAuditEnable(true)
        //设置 SQL 审计收集器
        AuditManager.setMessageCollector { auditMessage: AuditMessage ->
            log.info {
                "${auditMessage.fullSql},${auditMessage.elapsedTime}ms"
            }
        }

        // 添加监听器
        globalConfig.registerInsertListener({
            try {
                if (ObjectUtil.isNotNull(it) && it is BaseEntity) {
                    val current = if (ObjectUtil.isNotNull(it.createTime)) it.createTime else Date()
                    it.createTime = current
                    it.updateTime = current
                    val loginUser: LoginUser? = getLoginUser()
                    if (ObjectUtil.isNotNull(loginUser)) {
                        val userId: Long =
                            if (ObjectUtil.isNotNull(it.createBy)) it.createBy!! else loginUser!!.userId!!
                        // 当前已登录 且 创建人为空 则填充
                        it.createBy = userId
                        // 当前已登录 且 更新人为空 则填充
                        it.updateBy = userId
                        it.createDept = if (ObjectUtil.isNotNull(it.createDept)) it.createDept else loginUser?.deptId
                    }
                }
            } catch (e: Exception) {
                throw ServiceException("自动注入异常 => ${e.message}", HttpStatus.HTTP_UNAUTHORIZED)
            }
        }, BaseEntity::class.java)

        globalConfig.registerUpdateListener({
            try {
                if (ObjectUtil.isNotNull(it) && it is BaseEntity) {
                    val current = Date()
                    // 更新时间填充(不管为不为空)
                    it.updateTime = current
                    val loginUser = getLoginUser()
                    // 当前已登录 更新人填充(不管为不为空)
                    if (ObjectUtil.isNotNull(loginUser)) {
                        it.updateBy = loginUser!!.userId
                    }
                }
            } catch (e: java.lang.Exception) {
                throw ServiceException("自动注入异常 => " + e.message, HttpStatus.HTTP_UNAUTHORIZED)
            }
        }, BaseEntity::class.java)
    }
}
