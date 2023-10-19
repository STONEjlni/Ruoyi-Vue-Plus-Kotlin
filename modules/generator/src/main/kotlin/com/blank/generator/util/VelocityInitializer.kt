package com.blank.generator.util

import com.blank.common.core.constant.Constants
import org.apache.velocity.app.Velocity
import java.util.*

/**
 * VelocityEngine工厂
 */
object VelocityInitializer {
    /**
     * 初始化vm方法
     */
    fun initVelocity() {
        val p = Properties()
        try {
            // 加载classpath目录下的vm文件
            p.setProperty(
                "resource.loader.file.class",
                "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader"
            )
            // 定义字符集
            p.setProperty(Velocity.INPUT_ENCODING, Constants.UTF8)
            // 初始化Velocity引擎，指定配置Properties
            Velocity.init(p)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
