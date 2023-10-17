package com.blank.job.processors

import com.blank.common.core.annotation.Slf4j
import org.springframework.stereotype.Component
import tech.powerjob.common.utils.NetUtils
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.TaskResult
import tech.powerjob.worker.core.processor.sdk.BroadcastProcessor
import kotlin.math.max

/**
 * 广播处理器 示例
 */
@Slf4j
@Component
class BroadcastProcessorDemo : BroadcastProcessor {
    override fun preProcess(context: TaskContext): ProcessResult {
        println("===== BroadcastProcessorDemo#preProcess ======")
        context.omsLogger.info("BroadcastProcessorDemo#preProcess, current host: {}", NetUtils.getLocalHost())
        return if ("rootFailed" == context.jobParams) {
            ProcessResult(false, "console need failed")
        } else {
            ProcessResult(true)
        }
    }

    @Throws(Exception::class)
    override fun process(taskContext: TaskContext): ProcessResult {
        val logger = taskContext.omsLogger
        println("===== BroadcastProcessorDemo#process ======")
        logger.info("BroadcastProcessorDemo#process, current host: {}", NetUtils.getLocalHost())
        var sleepTime: Long = 1000
        try {
            sleepTime = taskContext.jobParams.toLong()
        } catch (e: Exception) {
            logger.warn("[BroadcastProcessor] parse sleep time failed!", e)
        }
        Thread.sleep(max(sleepTime.toDouble(), 1000.0).toLong())
        return ProcessResult(true)
    }

    override fun postProcess(context: TaskContext, taskResults: MutableList<TaskResult>?): ProcessResult {
        println("===== BroadcastProcessorDemo#postProcess ======")
        context.omsLogger.info(
            "BroadcastProcessorDemo#postProcess, current host: {}, taskResult: {}",
            NetUtils.getLocalHost(),
            taskResults
        )
        return ProcessResult(true, "success")
    }
}
