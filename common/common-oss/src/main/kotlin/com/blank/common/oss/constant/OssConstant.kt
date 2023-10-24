package com.blank.common.oss.constant

/**
 * 对象存储常量
 */
interface OssConstant {
    companion object {
        /**
         * 默认配置KEY
         */
        @JvmField
        val DEFAULT_CONFIG_KEY = "sys_oss:default_config"

        /**
         * 预览列表资源开关Key
         */
        @JvmField
        val PEREVIEW_LIST_RESOURCE_KEY = "sys.oss.previewListResource"

        /**
         * 系统数据ids
         */
        @JvmField
        val SYSTEM_DATA_IDS: MutableList<Long> = mutableListOf(1L, 2L, 3L, 4L)

        /**
         * 云服务商
         */
        @JvmField
        val CLOUD_SERVICE = arrayOf("aliyun", "qcloud", "qiniu", "obs")

        /**
         * https 状态
         */
        @JvmField
        val IS_HTTPS = "Y"
    }
}
