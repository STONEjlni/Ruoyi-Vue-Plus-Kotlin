package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.core.validate.QueryGroup
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysOssBo
import com.blank.system.domain.vo.SysOssUploadVo
import com.blank.system.domain.vo.SysOssVo
import com.blank.system.service.ISysOssService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.util.*

/**
 * 文件上传 控制层
 */
@Validated
@RestController
@RequestMapping("/resource/oss")
class SysOssController(
    private val ossService: ISysOssService
) : BaseController() {

    /**
     * 查询OSS对象存储列表
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/list")
    fun list(
        @Validated(
            QueryGroup::class
        ) bo: SysOssBo, pageQuery: PageQuery
    ): TableDataInfo<SysOssVo>? {
        return ossService.queryPageList(bo, pageQuery)
    }

    /**
     * 查询OSS对象基于id串
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:list")
    @GetMapping("/listByIds/{ossIds}")
    fun listByIds(@PathVariable ossIds: Array<Long>): R<MutableList<SysOssVo>> {
        val list = ossService.listByIds(ossIds.toMutableList())
        return ok(list)
    }

    /**
     * 上传OSS对象存储
     *
     * @param file 文件
     */
    @SaCheckPermission("system:oss:upload")
    @Log(title = "OSS对象存储", businessType = BusinessType.INSERT)
    @PostMapping(value = ["/upload"], consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestPart("file") file: MultipartFile): R<SysOssUploadVo> {
        if (ObjectUtil.isNull(file)) {
            return fail("上传文件不能为空")
        }
        val oss = ossService.upload(file)!!
        val uploadVo = SysOssUploadVo()
        uploadVo.url = oss.url
        uploadVo.fileName = oss.originalName
        uploadVo.ossId = oss.ossId.toString()
        return ok(uploadVo)
    }

    /**
     * 下载OSS对象
     *
     * @param ossId OSS对象ID
     */
    @SaCheckPermission("system:oss:download")
    @GetMapping("/download/{ossId}")
    @Throws(
        IOException::class
    )
    fun download(@PathVariable ossId: Long, response: HttpServletResponse) {
        ossService.download(ossId, response)
    }

    /**
     * 删除OSS对象存储
     *
     * @param ossIds OSS对象ID串
     */
    @SaCheckPermission("system:oss:remove")
    @Log(title = "OSS对象存储", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ossIds}")
    fun remove(@PathVariable ossIds: Array<Long>): R<Unit> {
        return toAjax(ossService.deleteWithValidByIds(ossIds.toMutableList(), true))
    }
}
