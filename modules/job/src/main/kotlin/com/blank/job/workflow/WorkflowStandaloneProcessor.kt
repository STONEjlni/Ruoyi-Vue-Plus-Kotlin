package com.blank.job.workflow

import com.alibaba.fastjson.JSON
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.BasicProcessor

/**
 * 工作流测试
 */
@Component
@Slf4j
class WorkflowStandaloneProcessor : BasicProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        val logger = context.omsLogger
        logger.info("current jobParams: {}", context.jobParams)
        logger.info("current context: {}", context.workflowContext)
        log.info { "jobParams:${context.jobParams}" }
        log.info { "currentContext:${JSON.toJSONString(context)}" }

        // 尝试获取上游任务
        val workflowContext = context.workflowContext.fetchWorkflowContext()
        log.info { "工作流上下文数据:$workflowContext" }
        return ProcessResult(true, "${context.jobId} process successfully.")
    }
}

