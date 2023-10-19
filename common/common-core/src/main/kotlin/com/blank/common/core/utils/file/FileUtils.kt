package com.blank.common.core.utils.file

import jakarta.servlet.http.HttpServletResponse
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

/**
 * 文件处理工具类
 */
object FileUtils {

    /**
     * 下载文件名重新编码
     *
     * @param response     响应对象
     * @param realFileName 真实文件名
     */
    fun setAttachmentResponseHeader(response: HttpServletResponse, realFileName: String) {
        val percentEncodedFileName = percentEncode(realFileName)
        val contentDispositionValue =
            "attachment; filename=$percentEncodedFileName;filename*=utf-8''$percentEncodedFileName"
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition,download-filename")
        response.setHeader("Content-disposition", contentDispositionValue)
        response.setHeader("download-filename", percentEncodedFileName)
    }

    /**
     * 百分号编码工具方法
     *
     * @param s 需要百分号编码的字符串
     * @return 百分号编码后的字符串
     */
    fun percentEncode(s: String?): String {
        val encode = URLEncoder.encode(s, StandardCharsets.UTF_8)
        return encode.replace("\\+".toRegex(), "%20")
    }
}
