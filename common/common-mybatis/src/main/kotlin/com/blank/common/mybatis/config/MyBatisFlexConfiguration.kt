package com.blank.common.mybatis.config

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.factory.YmlPropertySourceFactory
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.common.mybatis.listener.MyInsertListener
import com.blank.common.mybatis.listener.MyUpdateListener
import com.mybatisflex.core.FlexGlobalConfig
import com.mybatisflex.core.audit.AuditManager
import com.mybatisflex.core.audit.AuditMessage
import com.mybatisflex.spring.boot.MyBatisFlexCustomizer
import org.mybatis.spring.annotation.MapperScan
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.context.annotation.PropertySource
import org.springframework.transaction.annotation.EnableTransactionManagement

/**
 * mybatis-flex配置类
 */
@EnableTransactionManagement(proxyTargetClass = true)
@AutoConfiguration
@MapperScan("\${mybatis-flex.mapperPackage}")
@PropertySource(value = ["classpath:common-mybatis.yml"], factory = YmlPropertySourceFactory::class)
@Slf4j
class MyBatisFlexConfiguration : MyBatisFlexCustomizer {
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
        globalConfig.registerInsertListener(MyInsertListener(), BaseEntity::class.java)
        globalConfig.registerUpdateListener(MyUpdateListener(), BaseEntity::class.java)
    }
}
