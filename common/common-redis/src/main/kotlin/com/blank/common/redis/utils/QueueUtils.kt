package com.blank.common.redis.utils

import cn.hutool.extra.spring.SpringUtil
import org.redisson.api.RedissonClient
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

/**
 * 分布式队列工具
 * 轻量级队列 重量级数据量 请使用 MQ
 * 要求 redis 5.X 以上
 *
 */
object QueueUtils {
    private val CLIENT = SpringUtil.getBean(RedissonClient::class.java)

    /**
     * 获取客户端实例
     */
    fun getClient(): RedissonClient = CLIENT

    /**
     * 添加普通队列数据
     *
     * @param queueName 队列名
     * @param data      数据
     */
    fun <T> addQueueObject(queueName: String, data: T): Boolean {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        return queue.offer(data)
    }

    /**
     * 通用获取一个队列数据 没有数据返回 null(不支持延迟队列)
     *
     * @param queueName 队列名
     */
    fun <T> getQueueObject(queueName: String): T {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        return queue.poll()
    }

    /**
     * 通用删除队列数据(不支持延迟队列)
     */
    fun <T> removeQueueObject(queueName: String, data: T): Boolean {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        return queue.remove(data)
    }

    /**
     * 通用销毁队列 所有阻塞监听 报错(不支持延迟队列)
     */
    @JvmStatic
    fun <T> destroyQueue(queueName: String): Boolean {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        return queue.delete()
    }

    /**
     * 添加延迟队列数据 默认毫秒
     *
     * @param queueName 队列名
     * @param data      数据
     * @param time      延迟时间
     */
    fun <T> addDelayedQueueObject(queueName: String, data: T, time: Long) {
        addDelayedQueueObject(queueName, data, time, TimeUnit.MILLISECONDS)
    }

    /**
     * 添加延迟队列数据
     *
     * @param queueName 队列名
     * @param data      数据
     * @param time      延迟时间
     * @param timeUnit  单位
     */
    fun <T> addDelayedQueueObject(queueName: String, data: T, time: Long, timeUnit: TimeUnit?) {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        val delayedQueue = CLIENT.getDelayedQueue(queue)
        delayedQueue.offer(data, time, timeUnit)
    }

    /**
     * 获取一个延迟队列数据 没有数据返回 null
     *
     * @param queueName 队列名
     */
    fun <T> getDelayedQueueObject(queueName: String): T {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        val delayedQueue = CLIENT.getDelayedQueue(queue)
        return delayedQueue.poll()
    }

    /**
     * 删除延迟队列数据
     */
    fun <T> removeDelayedQueueObject(queueName: String, data: T): Boolean {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        val delayedQueue = CLIENT.getDelayedQueue(queue)
        return delayedQueue.remove(data)
    }

    /**
     * 销毁延迟队列 所有阻塞监听 报错
     */
    fun <T> destroyDelayedQueue(queueName: String) {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        val delayedQueue = CLIENT.getDelayedQueue(queue)
        delayedQueue.destroy()
    }

    /**
     * 添加优先队列数据
     *
     * @param queueName 队列名
     * @param data      数据
     */
    fun <T> addPriorityQueueObject(queueName: String, data: T): Boolean {
        val priorityBlockingQueue = CLIENT.getPriorityBlockingQueue<T>(queueName)
        return priorityBlockingQueue.offer(data)
    }

    /**
     * 优先队列获取一个队列数据 没有数据返回 null(不支持延迟队列)
     *
     * @param queueName 队列名
     */
    fun <T> getPriorityQueueObject(queueName: String): T {
        val queue = CLIENT.getPriorityBlockingQueue<T>(queueName)
        return queue.poll()
    }

    /**
     * 优先队列删除队列数据(不支持延迟队列)
     */
    fun <T> removePriorityQueueObject(queueName: String, data: T): Boolean {
        val queue = CLIENT.getPriorityBlockingQueue<T>(queueName)
        return queue.remove(data)
    }

    /**
     * 优先队列销毁队列 所有阻塞监听 报错(不支持延迟队列)
     */
    fun <T> destroyPriorityQueue(queueName: String): Boolean {
        val queue = CLIENT.getPriorityBlockingQueue<T>(queueName)
        return queue.delete()
    }

    /**
     * 尝试设置 有界队列 容量 用于限制数量
     *
     * @param queueName 队列名
     * @param capacity  容量
     */
    fun <T> trySetBoundedQueueCapacity(queueName: String, capacity: Int): Boolean {
        val boundedBlockingQueue = CLIENT.getBoundedBlockingQueue<T>(queueName)
        return boundedBlockingQueue.trySetCapacity(capacity)
    }

    /**
     * 尝试设置 有界队列 容量 用于限制数量
     *
     * @param queueName 队列名
     * @param capacity  容量
     * @param destroy   已存在是否销毁
     */
    fun <T> trySetBoundedQueueCapacity(queueName: String, capacity: Int, destroy: Boolean): Boolean {
        val boundedBlockingQueue = CLIENT.getBoundedBlockingQueue<T>(queueName)
        if (boundedBlockingQueue.isExists && destroy) {
            destroyQueue<Any>(queueName)
        }
        return boundedBlockingQueue.trySetCapacity(capacity)
    }

    /**
     * 添加有界队列数据
     *
     * @param queueName 队列名
     * @param data      数据
     * @return 添加成功 true 已达到界限 false
     */
    fun <T> addBoundedQueueObject(queueName: String, data: T): Boolean {
        val boundedBlockingQueue = CLIENT.getBoundedBlockingQueue<T>(queueName)
        return boundedBlockingQueue.offer(data)
    }

    /**
     * 有界队列获取一个队列数据 没有数据返回 null(不支持延迟队列)
     *
     * @param queueName 队列名
     */
    fun <T> getBoundedQueueObject(queueName: String): T {
        val queue = CLIENT.getBoundedBlockingQueue<T>(queueName)
        return queue.poll()
    }

    /**
     * 有界队列删除队列数据(不支持延迟队列)
     */
    fun <T> removeBoundedQueueObject(queueName: String, data: T): Boolean {
        val queue = CLIENT.getBoundedBlockingQueue<T>(queueName)
        return queue.remove(data)
    }

    /**
     * 有界队列销毁队列 所有阻塞监听 报错(不支持延迟队列)
     */
    fun <T> destroyBoundedQueue(queueName: String): Boolean {
        val queue = CLIENT.getBoundedBlockingQueue<T>(queueName)
        return queue.delete()
    }

    /**
     * 订阅阻塞队列(可订阅所有实现类 例如: 延迟 优先 有界 等)
     */
    fun <T> subscribeBlockingQueue(queueName: String, consumer: Consumer<T>) {
        val queue = CLIENT.getBlockingQueue<T>(queueName)
        queue.subscribeOnElements(consumer)
    }
}
