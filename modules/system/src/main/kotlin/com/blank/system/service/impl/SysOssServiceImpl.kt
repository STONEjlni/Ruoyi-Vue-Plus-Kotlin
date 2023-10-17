package com.blank.system.service.impl

import cn.hutool.core.convert.Convert
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.OssService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.SpringUtilExtend.getAopProxy
import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.StringUtilsExtend.splitTo
import com.blank.common.core.utils.file.FileUtils.setAttachmentResponseHeader
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.oss.entity.UploadResult
import com.blank.common.oss.enumd.AccessPolicyType
import com.blank.common.oss.factory.OssFactory.instance
import com.blank.system.domain.SysOss
import com.blank.system.domain.bo.SysOssBo
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
    override fun queryPageList(bo: SysOssBo, pageQuery: PageQuery): TableDataInfo<SysOssVo>? {
        /*LambdaQueryWrapper<SysOss> lqw = buildQueryWrapper(bo);
        Page<SysOssVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        List<SysOssVo> filterResult = StreamUtils.toList(result.getRecords(), this::matchingUrl);
        result.setRecords(filterResult);
        return TableDataInfo.build(result);*/
        return null
    }

    override fun listByIds(ossIds: MutableCollection<Long>): MutableList<SysOssVo>? {
        val list: MutableList<SysOssVo> = ArrayList()
        for (id in ossIds) {
            val vo = getAopProxy(this).getById(id)
            if (ObjectUtil.isNotNull(vo)) {
                list.add(matchingUrl(vo!!))
            }
        }
        return list
    }

    override fun selectUrlByIds(ossIds: String): String? {
        val list: MutableList<String> = ArrayList()
        for (id in splitTo<Long>(ossIds!!) { value: Any? -> Convert.toLong(value) }) {
            val vo = getAopProxy(this).getById(id)
            if (ObjectUtil.isNotNull(vo)) {
                list.add(matchingUrl(vo!!).url!!)
            }
        }
        return java.lang.String.join(StringUtilsExtend.SEPARATOR, list)
    }

    private fun  /*<SysOss>*/buildQueryWrapper(bo: SysOssBo): QueryWrapper? {
        /*Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysOss> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getFileName()), SysOss::getFileName, bo.getFileName());
        lqw.like(StrUtil.isNotBlank(bo.getOriginalName()), SysOss::getOriginalName, bo.getOriginalName());
        lqw.eq(StrUtil.isNotBlank(bo.getFileSuffix()), SysOss::getFileSuffix, bo.getFileSuffix());
        lqw.eq(StrUtil.isNotBlank(bo.getUrl()), SysOss::getUrl, bo.getUrl());
        lqw.between(params.get("beginCreateTime") != null && params.get("endCreateTime") != null,
            SysOss::getCreateTime, params.get("beginCreateTime"), params.get("endCreateTime"));
        lqw.eq(ObjectUtil.isNotNull(bo.getCreateBy()), SysOss::getCreateBy, bo.getCreateBy());
        lqw.eq(StrUtil.isNotBlank(bo.getService()), SysOss::getService, bo.getService());
        lqw.orderByAsc(SysOss::getOssId);
        return lqw;*/
        return null
    }

    @Cacheable(cacheNames = [CacheNames.SYS_OSS], key = "#ossId")
    override fun getById(ossId: Long): SysOssVo? {
        return baseMapper.selectVoById(ossId)
    }

    @Throws(IOException::class)
    override fun download(ossId: Long, response: HttpServletResponse) {
        val sysOss = getAopProxy(this).getById(ossId)!!
        if (ObjectUtil.isNull(sysOss)) {
            throw ServiceException("文件数据不存在!")
        }
        setAttachmentResponseHeader(response, sysOss.originalName!!)
        response.contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE + "; charset=UTF-8"
        val storage = instance(sysOss.service!!)
        try {
            storage!!.getObjectContent(sysOss.url!!).use { inputStream ->
                val available = inputStream.available()
                IoUtil.copy(inputStream, response.outputStream, available)
                response.setContentLength(available)
            }
        } catch (e: Exception) {
            throw ServiceException(e.message)
        }
    }

    override fun upload(file: MultipartFile): SysOssVo? {
        val originalfileName = file.originalFilename
        val suffix =
            StringUtils.substring(originalfileName, originalfileName!!.lastIndexOf("."), originalfileName.length)
        val storage = instance()
        val uploadResult: UploadResult
        uploadResult = try {
            storage!!.uploadSuffix(file.bytes, suffix, file.contentType)
        } catch (e: IOException) {
            throw ServiceException(e.message)
        }
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult)
    }

    override fun upload(file: File): SysOssVo? {
        val originalfileName = file.getName()
        val suffix = StringUtils.substring(originalfileName, originalfileName.lastIndexOf("."), originalfileName.length)
        val storage = instance()
        val uploadResult = storage!!.uploadSuffix(file, suffix)
        // 保存文件信息
        return buildResultEntity(originalfileName, suffix, storage.getConfigKey(), uploadResult)
    }

    private fun buildResultEntity(
        originalfileName: String?,
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
        baseMapper!!.insert(oss)
        val sysOssVo = convert(oss, SysOssVo::class.java)!!
        return matchingUrl(sysOssVo)
    }

    override fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean {
        /*if (isValid) {
            // 做一些业务上的校验,判断是否需要校验
        }
        List<SysOss> list = baseMapper.selectBatchIds(ids);
        for (SysOss sysOss : list) {
            OssClient storage = OssFactory.instance(sysOss.getService());
            storage.delete(sysOss.getUrl());
        }
        return baseMapper.deleteBatchIds(ids) > 0;*/
        return false
    }

    /**
     * 匹配Url
     *
     * @param oss OSS对象
     * @return oss 匹配Url的OSS对象
     */
    private fun matchingUrl(oss: SysOssVo): SysOssVo {
        val storage = instance(oss.service!!)
        // 仅修改桶类型为 private 的URL，临时URL时长为120s
        if (AccessPolicyType.PRIVATE === storage!!.getAccessPolicy()) {
            oss.url = storage!!.getPrivateUrl(oss.fileName, 120)
        }
        return oss
    }
}
