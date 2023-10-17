package com.blank

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup
import org.springframework.boot.runApplication

/**
 * 启动程序
 */
@SpringBootApplication
class SiteNavApplication

fun main(args: Array<String>) {

    val application = runApplication<SiteNavApplication>(*args)
    application.applicationStartup = BufferingApplicationStartup(2048)
    println("(♥◠‿◠)ﾉﾞ  RuoYi-Vue-Plus-Kotlin启动成功   ლ(´ڡ`ლ)ﾞ")
}
