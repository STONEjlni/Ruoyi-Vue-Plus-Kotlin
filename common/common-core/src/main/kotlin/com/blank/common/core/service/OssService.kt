package com.blank.common.core.service

/**
 * 通用 OSS服务
 */
interface OssService {
    /**
     * 通过ossId查询对应的url
     *
     * @param ossIds ossId串逗号分隔
     * @return url串逗号分隔
     */
    fun selectUrlByIds(ossIds: String): String?
}
