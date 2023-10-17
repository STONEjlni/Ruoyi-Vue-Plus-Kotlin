package com.blank.job.processors

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.BasicProcessor

/**
 * 测试超时任务（可中断）
 */
@Component
@Slf4j
class TimeoutProcessor : BasicProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        val sleepTime = context.jobParams.toLong()
        log.info { "TaskInstance(${context.instanceId}) will sleep $sleepTime ms" }
        Thread.sleep(context.jobParams.toLong())
        return ProcessResult(true, "impossible~~~~QAQ~")
    }
}

