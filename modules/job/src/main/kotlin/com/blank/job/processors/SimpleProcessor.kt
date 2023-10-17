package com.blank.job.processors

import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.BasicProcessor
import java.util.*

/**
 *
 */
@Component
class SimpleProcessor : BasicProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        val logger = context.omsLogger
        val jobParams = Optional.ofNullable(context.jobParams).orElse("S")
        logger.info("Current context:{}", context.workflowContext)
        logger.info("Current job params:{}", jobParams)

        // 测试中文问题 #581
        if (jobParams.contains("CN")) {
            return ProcessResult(true, "任务成功啦！！！")
        }
        return if (jobParams.contains("F")) ProcessResult(false) else ProcessResult(true, "yeah!")
    }
}

