package com.blank.job.processors

import com.alibaba.fastjson.JSONObject
import org.springframework.stereotype.Component
import tech.powerjob.official.processors.util.CommonUtils
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.BasicProcessor
import java.util.*

/**
 * LogTestProcessor
 */
@Component
class LogTestProcessor : BasicProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        val omsLogger = context.omsLogger
        val parseParams = CommonUtils.parseParams(context)
        val config = Optional.ofNullable(JSONObject.parseObject(parseParams)).orElse(JSONObject())
        val loopTimes = Optional.ofNullable(config.getLong("loopTimes")).orElse(1000L)
        for (i in 0 until loopTimes) {
            omsLogger.debug("[DEBUG] one DEBUG log in {}", Date())
            omsLogger.info("[INFO] one INFO log in {}", Date())
            omsLogger.warn("[WARN] one WARN log in {}", Date())
            omsLogger.error("[ERROR] one ERROR log in {}", Date())
        }
        return ProcessResult(true)
    }
}

