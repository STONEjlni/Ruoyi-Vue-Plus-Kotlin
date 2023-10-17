package com.blank.common.core.utils

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import java.util.concurrent.*

/**
 * 线程相关工具类.
 */
@Slf4j
object Threads {
    /**
     * sleep等待,单位为毫秒
     */
    @JvmStatic
    fun sleep(milliseconds: Long) {
        try {
            Thread.sleep(milliseconds)
        } catch (e: InterruptedException) {
            return
        }
    }

    /**
     * 停止线程池
     * 先使用shutdown, 停止接收新任务并尝试完成所有已存在任务.
     * 如果超时, 则调用shutdownNow, 取消在workQueue中Pending的任务,并中断所有阻塞函数.
     * 如果仍然超時，則強制退出.
     * 另对在shutdown时线程本身被调用中断做了处理.
     */
    @JvmStatic
    fun shutdownAndAwaitTermination(pool: ExecutorService?) {
        if (pool != null && !pool.isShutdown) {
            pool.shutdown()
            try {
                if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                    pool.shutdownNow()
                    if (!pool.awaitTermination(120, TimeUnit.SECONDS)) {
                        log.info { "Pool did not terminate" }
                    }
                }
            } catch (ie: InterruptedException) {
                pool.shutdownNow()
                Thread.currentThread().interrupt()
            }
        }
    }

    /**
     * 打印线程异常信息
     */
    @JvmStatic
    fun printException(r: Runnable?, t: Throwable?) {
        var t = t
        if (t == null && r is Future<*>) {
            try {
                val future = r
                if (future.isDone) {
                    future.get()
                }
            } catch (ce: CancellationException) {
                t = ce
            } catch (ee: ExecutionException) {
                t = ee.cause
            } catch (ie: InterruptedException) {
                Thread.currentThread().interrupt()
            }
        }
        if (t != null) {
            log.error(t) { "${t.message}" }
        }
    }

}
