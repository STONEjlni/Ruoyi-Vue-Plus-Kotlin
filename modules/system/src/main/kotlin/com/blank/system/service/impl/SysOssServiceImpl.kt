package com.blank.system.service.impl

import cn.hutool.core.convert.Convert
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.OssService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.SpringUtilExtend.getAopProxy
import com.blank.common.core.utils.StreamUtils
import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.StringUtilsExtend.splitTo
import com.blank.common.core.utils.file.FileUtils.setAttachmentResponseHeader
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.oss.core.OssClient
import com.blank.common.oss.entity.UploadResult
import com.blank.common.oss.enumd.AccessPolicyType
import com.blank.common.oss.factory.OssFactory.instance
import com.blank.system.domain.SysOss
import com.blank.system.domain.bo.SysOssBo
import com.blank.system.domain.table.SysOssDef.SYS_OSS
import com.blank.system.domain.vo.SysOssVo
import com.blank.system.mapper.SysOssMapper
import com.blank.system.service.ISysOssService
import com.mybatisflex.core.query.QueryWrapper
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.IOException

/**
 * 文件上传 服务层实现
 */
@Service
class SysOssServiceImpl(
    private val baseMapper: SysOssMapper
) : ISysOssService, OssService {

    override fun queryPageList(bo: SysOssBo, pageQuery: PageQuery): TableDataInfo<SysOssVo> {
        val lqw = buildQueryWrapper(bo)
        val result = baseMapper.paginateAs(pageQuery, lqw, SysOssVo::class.java)
        val filterResult: MutableList<SysOssVo> = StreamUtils.toList(result.records) { oss: SysOssVo ->
            matchingUrl(
                oss
            )
        }
        result.records = filterResult
        return TableDataInfo.build(result)
    }

    override fun listByIds(ossIds: MutableCollection<Long>): MutableList<SysOssVo> {
        val list: MutableList<SysOssVo> = ArrayList()
        for (id in ossIds) {
            val vo: SysOssVo? = getAopProxy(this).getById(id)
            if (ObjectUtil.isNotNull(vo)) {
                list.add(matchingUrl(vo!!))
            }
        }
        return list
    }

    override fun selectUrlByIds(ossIds: String): String {
        val list: MutableList<String> = ArrayList()
        for (id in splitTo(ossIds, Convert::toLong)) {
            val vo: SysOssVo? = getAopProxy(this).getById(id)
            if (ObjectUtil.isNotNull(vo)) {
                list.add(matchingUrl(vo!!).url!!)
            }
        }
        return list.joinToString(separator = StringUtilsExtend.SEPARATOR) { it }
    }

    private fun buildQueryWrapper(bo: SysOssBo): QueryWrapper {
        val params: Map<String, Any?> = bo.params
        return QueryWrapper.create().from(SYS_OSS)
            .where(SYS_OSS.FILE_NAME.like(bo.fileName))
            .and(SYS_OSS.ORIGINAL_NAME.like(bo.originalName))
            .and(SYS_OSS.FILE_SUFFIX.eq(bo.fileSuffix))
            .and(SYS_OSS.URL.eq(bo.url))
            .and(
                SYS_OSS.CREATE_TIME.between(
                    params["beginCreateTime"],
                    params["endCreateTime"],
                    params["beginCreateTime"] != null && params["endCreateTime"] != null
                )
            )
            .and(SYS_OSS.CREATE_BY.eq(bo.createBy))
            .and(SYS_OSS.SERVICE.eq(bo.service))
            .orderBy(SYS_OSS.OSS_ID, true)
    }

    @Cacheable(cacheNames = [CacheNames.SYS_OSS], key = "#ossId")
    override fun getById(ossId: Long): SysOssVo? {
        return baseMapper.selectOneWithRelationsByIdAs(ossId, SysOssVo::class.java)
    }

    @Throws(IOException::class)
    override fun download(ossId: Long, response: HttpServletResponse) {
        val sysOss: SysOssVo = getAopProxy(this).getById(ossId)!!
        if (ObjectUtil.isNull(sysOss)) {
            throw ServiceException("文件数据不存在!")
        }
        setAttachmentResponseHeader(response, sysOss.originalName!!)
        response.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8"
        val storage: OssClient = instance(sysOss.service!!)
        try {
            storage.getObjectContent(sysOss.url!!).use { inputStream ->
                val available: Int = inputStream.available()
                IoUtil.copy(inputStream, response.outputStream, available)
                response.setContentLength(available)
            }
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    override fun upload(file: MultipartFile): SysOssVo {
        val originalfileName = file.originalFilename
        val suffix =
            StringUtils.substring(originalfileName, originalfileName!!.lastIndexOf("."), originalfileName.length)
        val storage: OssClient = instance()
        val uploadResult: UploadResult = try {
            storage.uploadSuffix(file.bytes, suffix, file.contentType)
        } catch (e: IOException) {
            throw ServiceException(e.message)
        }
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult)
    }

    override fun upload(file: File): SysOssVo {
        val originalfileName = file.getName()
        val suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length)
        val storage: OssClient = instance()
        val uploadResult: UploadResult = storage.uploadSuffix(file, suffix)
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult)
    }

    private fun buildResultEntity(
        originalfileName: String,
        suffix: String,
        configKey: String,
        uploadResult: UploadResult
    ): SysOssVo {
        val oss = SysOss()
        oss.url = uploadResult.url
        oss.fileSuffix = suffix
        oss.fileName = uploadResult.filename
        oss.originalName = originalfileName
        oss.service = configKey
        baseMapper.insert(oss, true)
        val sysOssVo: SysOssVo = convert(oss, SysOssVo::class.java)!!
        return matchingUrl(sysOssVo)
    }

    override fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean {
        if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        val list = baseMapper.selectListByIds(ids)
        for (sysOss in list) {
            val storage: OssClient = instance(sysOss.service!!)
            storage.delete(sysOss.url!!)
        }
        return baseMapper.deleteBatchByIds(ids) > 0
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private fun matchingUrl(oss: SysOssVo): SysOssVo {
        val storage: OssClient = instance(oss.service!!)
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE === storage.getAccessPolicy()) {
            oss.url = storage.getPrivateUrl(oss.fileName, 120)
        }
        return oss
    }
}
