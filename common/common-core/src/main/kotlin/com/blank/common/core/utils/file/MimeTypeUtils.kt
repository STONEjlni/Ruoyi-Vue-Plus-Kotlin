package com.blank.common.core.utils.file

/**
 * 媒体类型工具类
 */
object MimeTypeUtils {
    const val IMAGE_PNG = "image/png"

    const val IMAGE_JPG = "image/jpg"

    const val IMAGE_JPEG = "image/jpeg"

    const val IMAGE_BMP = "image/bmp"

    const val IMAGE_GIF = "image/gif"

    var IMAGE_EXTENSION = arrayOf("bmp", "gif", "jpg", "jpeg", "png")

    val FLASH_EXTENSION = arrayOf("swf", "flv")

    val MEDIA_EXTENSION = arrayOf(
        "swf", "flv", "mp3", "wav", "wma", "wmv", "mid", "avi", "mpg",
        "asf", "rm", "rmvb"
    )

    val VIDEO_EXTENSION = arrayOf("mp4", "avi", "rmvb")

    val DEFAULT_ALLOWED_EXTENSION = arrayOf( // 图片
        "bmp", "gif", "jpg", "jpeg", "png",  // word excel powerpoint
        "doc", "docx", "xls", "xlsx", "ppt", "pptx", "html", "htm", "txt",  // 压缩文件
        "rar", "zip", "gz", "bz2",  // 视频格式
        "mp4", "avi", "rmvb",  // pdf
        "pdf"
    )
}
