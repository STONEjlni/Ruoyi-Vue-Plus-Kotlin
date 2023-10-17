package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysOssBo
import com.blank.system.domain.vo.SysOssVo
import jakarta.servlet.http.HttpServletResponse
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException

/**
 * 文件上传 服务层
 */
interface ISysOssService {
    fun queryPageList(sysOss: SysOssBo, pageQuery: PageQuery): TableDataInfo<SysOssVo>?
    fun listByIds(ossIds: MutableCollection<Long>): MutableList<SysOssVo>?
    fun getById(ossId: Long): SysOssVo?
    fun upload(file: MultipartFile): SysOssVo?
    fun upload(file: File): SysOssVo?

    @Throws(IOException::class)
    fun download(ossId: Long, response: HttpServletResponse)
    fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean
}
