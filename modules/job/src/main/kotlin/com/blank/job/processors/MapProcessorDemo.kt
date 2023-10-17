package com.blank.job.processors

import com.blank.common.json.utils.JsonUtils.toJsonString
import com.google.common.collect.Lists
import org.springframework.stereotype.Component
import tech.powerjob.worker.core.processor.ProcessResult
import tech.powerjob.worker.core.processor.TaskContext
import tech.powerjob.worker.core.processor.sdk.MapProcessor
import java.util.concurrent.ThreadLocalRandom

/**
 * Map处理器 示例
 */
@Component
class MapProcessorDemo : MapProcessor {
    @Throws(Exception::class)
    override fun process(context: TaskContext): ProcessResult {
        MapProcessor.log.info("============== MapProcessorDemo#process ==============")
        MapProcessor.log.info("isRootTask:{}", isRootTask)
        MapProcessor.log.info("taskContext:{}", toJsonString(context))
        return if (isRootTask) {
            MapProcessor.log.info("==== MAP ====")
            val subTasks: MutableList<SubTask> = Lists.newLinkedList()
            for (j in 0 until BATCH_NUM) {
                val subTask = SubTask()
                subTask.siteId = j
                subTask.itemIds = Lists.newLinkedList()
                subTasks.add(subTask)
                for (i in 0 until BATCH_SIZE) {
                    subTask.itemIds!!.add(i + j * 100)
                }
            }
            map(subTasks, "MAP_TEST_TASK")
            ProcessResult(true, "map successfully")
        } else {
            MapProcessor.log.info("==== PROCESS ====")
            val subTask = context.subTask as SubTask
            for (itemId in subTask.itemIds!!) {
                if (Thread.interrupted()) {
                    // 任务被中断
                    MapProcessor.log.info(
                        "job has been stop! so stop to process subTask: {} => {}",
                        subTask.siteId,
                        itemId
                    )
                    break
                }
                MapProcessor.log.info("processing subTask: {} => {}", subTask.siteId, itemId)
                val max = Int.MAX_VALUE shr 7
                var i = 0
                while (true) {

                    // 模拟耗时操作
                    if (i > max) {
                        break
                    }
                    i++
                }
            }
            // 测试在 Map 任务中追加上下文
            context.workflowContext.appendData2WfContext("Yasuo", "A sword's poor company for a long road.")
            var b = ThreadLocalRandom.current().nextBoolean()
            if (context.currentRetryTimes >= 1) {
                // 重试的话一定会成功
                b = true
            }
            ProcessResult(b, "RESULT:$b")
        }
    }

    class SubTask(
        var siteId: Int? = null,
        var itemIds: MutableList<Int>? = null
    )

    companion object {
        /**
         * 每一批发送任务大小
         */
        private const val BATCH_SIZE = 100

        /**
         * 发送的批次
         */
        private const val BATCH_NUM = 5
    }
}

