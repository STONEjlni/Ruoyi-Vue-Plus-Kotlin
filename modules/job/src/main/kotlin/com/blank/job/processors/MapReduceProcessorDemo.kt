package com.blank.job.processors

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.json.utils.JsonUtils.parseMap
import com.blank.common.json.utils.JsonUtils.toJsonString
import com.google.common.collect.Lists
import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.TaskResult
import tech.powerjob.worker.core.processor.sdk.MapReduceProcessor
import java.util.concurrent.ThreadLocalRandom

/**
 * MapReduce 处理器示例
 * 控制台参数：{"batchSize": 100, "batchNum": 2}
 */
@Slf4j
@Component
class MapReduceProcessorDemo : MapReduceProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        val omsLogger = context.omsLogger
        log.info { "============== TestMapReduceProcessor#process ==============" }
        log.info { "isRootTask:$isRootTask" }
        log.info { "taskContext:${toJsonString(context)}" }

        // 根据控制台参数获取MR批次及子任务大小
        val jobParams = parseMap(context.jobParams)
        val batchSize = jobParams!!.getOrDefault("batchSize", 100) as Int
        val batchNum = jobParams.getOrDefault("batchNum", 10) as Int
        return if (isRootTask) {
            log.info { "==== MAP ====" }
            omsLogger.info("[DemoMRProcessor] start root task~")
            val subTasks: MutableList<TestSubTask?> = Lists.newLinkedList()
            for (j in 0 until batchNum) {
                for (i in 0 until batchSize) {
                    val x = j * batchSize + i
                    subTasks.add(TestSubTask("name$x", x))
                }
                map(subTasks, "MAP_TEST_TASK")
                subTasks.clear()
            }
            omsLogger.info("[DemoMRProcessor] map success~")
            ProcessResult(true, "MAP_SUCCESS")
        } else {
            log.info { "==== NORMAL_PROCESS ====" }
            omsLogger.info("[DemoMRProcessor] process subTask: {}.", toJsonString(context.subTask))
            log.info { "subTask: ${toJsonString(context.subTask)}" }
            Thread.sleep(1000)
            if (context.currentRetryTimes == 0) {
                ProcessResult(false, "FIRST_FAILED")
            } else {
                ProcessResult(true, "PROCESS_SUCCESS")
            }
        }
    }

    override fun reduce(context: TaskContext, taskResults: MutableList<TaskResult>): ProcessResult {
        log.info { "================ MapReduceProcessorDemo#reduce ================" }
        log.info { "TaskContext: ${toJsonString(context)}" }
        log.info { "List<TaskResult>: ${toJsonString(taskResults)}" }
        context.omsLogger.info("MapReduce job finished, result is {}.", taskResults)
        val success = ThreadLocalRandom.current().nextBoolean()
        return ProcessResult(success, "$context: $success")
    }

    class TestSubTask(
        var name: String? = null,
        var age: Int = 0
    )
}

