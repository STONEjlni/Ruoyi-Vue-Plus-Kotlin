package com.blank.system.runner

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.system.service.ISysOssConfigService
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

/**
 * 初始化 system 模块对应业务数据
 */
@Slf4j
@Component
class SystemApplicationRunner(
    private val ossConfigService: ISysOssConfigService
) : ApplicationRunner {
    @Throws(Exception::class)
    override fun run(args: ApplicationArguments) {
        ossConfigService.init()
        log.info { "初始化OSS配置成功" }
    }
}
