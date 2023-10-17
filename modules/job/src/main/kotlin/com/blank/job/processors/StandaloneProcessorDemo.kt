package com.blank.job.processors

import cn.hutool.core.util.StrUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.BasicProcessor
import java.util.*

/**
 * 单机处理器 示例
 */
@Slf4j
@Component
class StandaloneProcessorDemo : BasicProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        val omsLogger = context.omsLogger
        omsLogger.info("StandaloneProcessorDemo start process,context is {}.", context)
        omsLogger.info("Notice! If you want this job process failed, your jobParams need to be 'failed'")
        omsLogger.info("Let's test the exception~")
        // 测试异常日志
        try {
            Collections.emptyList<String>().add("277")
        } catch (e: Exception) {
            omsLogger.error("oh~it seems that we have an exception~", e)
        }
        log.info { "================ StandaloneProcessorDemo#process ================" }
        log.info { "jobParam:${context.jobParams}" }
        log.info { "instanceParams:${context.instanceParams}" }
        val param: String
        // 解析参数，非处于工作流中时，优先取实例参数（允许动态[instanceParams]覆盖静态参数[jobParams]）
        param = if (context.workflowContext == null) {
            if (StrUtil.isBlank(context.instanceParams)) context.jobParams else context.instanceParams
        } else {
            context.jobParams
        }
        // 根据参数判断是否成功
        val success = "failed" != param
        omsLogger.info("StandaloneProcessorDemo finished process,success: {}", success)
        omsLogger.info("anyway, we finished the job successfully~Congratulations!")
        return ProcessResult(success, "$context: $success")
    }
}

