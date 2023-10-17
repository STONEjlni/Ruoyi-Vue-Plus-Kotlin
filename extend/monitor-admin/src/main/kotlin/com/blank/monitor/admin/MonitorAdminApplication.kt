package com.blank.monitor.admin

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Admin 监控启动程序
 */
@SpringBootApplication
class MonitorAdminApplication

fun main(args: Array<String>) {
    runApplication<MonitorAdminApplication>(*args)
}

