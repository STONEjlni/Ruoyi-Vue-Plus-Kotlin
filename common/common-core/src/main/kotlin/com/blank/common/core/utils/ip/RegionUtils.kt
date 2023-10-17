package com.blank.common.core.utils.ip

import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.resource.ClassPathResource
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.exception.ServiceException
import org.lionsoul.ip2region.xdb.Searcher
import java.io.File

/**
 * 根据ip地址定位工具类，离线方式
 * 参考地址：<a href="https://gitee.com/lionsoul/ip2region/tree/master/binding/java">集成 ip2region 实现离线IP地址定位库</a>
 */
@Slf4j
object RegionUtils {

    private val SEARCHER: Searcher

    init {
        val fileName = "/ip2region.xdb"
        val existFile: File = FileUtil.file(FileUtil.getTmpDir().toString() + FileUtil.FILE_SEPARATOR + fileName)
        if (!FileUtil.exist(existFile)) {
            val fileStream = ClassPathResource(fileName)
            if (ObjectUtil.isEmpty(fileStream.stream)) {
                throw ServiceException("RegionUtils初始化失败，原因：IP地址库数据不存在！")
            }
            FileUtil.writeFromStream(fileStream.stream, existFile)
        }

        val dbPath = existFile.path

        // 1、从 dbPath 加载整个 xdb 到内存。
        val cBuff = try {
            Searcher.loadContentFromFile(dbPath)
        } catch (e: Exception) {
            throw ServiceException("RegionUtils初始化失败，原因：从ip2region.xdb文件加载内容失败！" + e.message)
        }

        // 2、使用上述的 cBuff 创建一个完全基于内存的查询对象。
        try {
            SEARCHER = Searcher.newWithBuffer(cBuff)
        } catch (e: Exception) {
            throw ServiceException("RegionUtils初始化失败，原因：$${e.message}")
        }
    }

    /**
     * 根据IP地址离线获取城市
     */
    fun getCityInfo(ip: String): String {
        var ip = ip
        return try {
            ip = ip.trim { it <= ' ' }
            // 3、执行查询
            val region = SEARCHER.search(ip)
            region.replace("0|", "").replace("|0", "")
        } catch (e: java.lang.Exception) {
            log.error { "IP地址离线获取城市异常 $ip" }
            "未知"
        }
    }
}
