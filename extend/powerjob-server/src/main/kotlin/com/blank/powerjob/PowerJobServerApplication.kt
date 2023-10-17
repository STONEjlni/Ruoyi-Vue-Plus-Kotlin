package com.blank.powerjob

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling
import tech.powerjob.server.common.utils.PropertyUtils

/**
 * powerjob 启动程序
 */
@EnableScheduling
@SpringBootApplication(scanBasePackages = ["tech.powerjob.server"])
class PowerJobServerApplication

fun main(args: Array<String>) {
    PropertyUtils.init()
    runApplication<PowerJobServerApplication>(*args)
    println("文档地址: https://www.yuque.com/powerjob/guidence/problem")
}


