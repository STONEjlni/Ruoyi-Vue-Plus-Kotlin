package com.blank.system.service.impl

import cn.dev33.satoken.context.SaHolder
import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.CacheConstants
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.DictService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.SpringUtilExtend.getAopProxy
import com.blank.common.core.utils.StreamUtils.toMap
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.redis.utils.CacheUtils
import com.blank.system.domain.SysDictData
import com.blank.system.domain.SysDictType
import com.blank.system.domain.bo.SysDictTypeBo
import com.blank.system.domain.table.SysDictDataDef.SYS_DICT_DATA
import com.blank.system.domain.table.SysDictTypeDef.SYS_DICT_TYPE
import com.blank.system.domain.vo.SysDictDataVo
import com.blank.system.domain.vo.SysDictTypeVo
import com.blank.system.mapper.SysDictDataMapper
import com.blank.system.mapper.SysDictTypeMapper
import com.blank.system.service.ISysDictTypeService
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.update.UpdateChain
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors

/**
 * 字典 业务层处理
 */
@Service
class SysDictTypeServiceImpl(
    private val baseMapper: SysDictTypeMapper,
    private val dictDataMapper: SysDictDataMapper
) : ISysDictTypeService, DictService {

    override fun selectPageDictTypeList(dictType: SysDictTypeBo, pageQuery: PageQuery): TableDataInfo<SysDictTypeVo> {
        val lqw = buildQueryWrapper(dictType)
        val page = baseMapper.paginateAs(pageQuery, lqw, SysDictTypeVo::class.java)
        return TableDataInfo.build(page)
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    override fun selectDictTypeList(dictType: SysDictTypeBo): MutableList<SysDictTypeVo> {
        val lqw = buildQueryWrapper(dictType)
        return baseMapper.selectListByQueryAs(lqw, SysDictTypeVo::class.java)
    }

    private fun buildQueryWrapper(bo: SysDictTypeBo): QueryWrapper {
        val params: Map<String, Any> = bo.params
        return QueryWrapper.create().from(SYS_DICT_TYPE)
            .where(SYS_DICT_TYPE.DICT_NAME.like(bo.dictName))
            .and(SYS_DICT_TYPE.DICT_TYPE.like(bo.dictType))
            .and(
                SYS_DICT_TYPE.CREATE_TIME.between(
                    params["beginTime"],
                    params["endTime"],
                    params["beginTime"] != null && params["endTime"] != null
                )
            )
            .orderBy(SYS_DICT_TYPE.DICT_ID, true)
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    override fun selectDictTypeAll(): MutableList<SysDictTypeVo> {
        return baseMapper.selectListByQueryAs(QueryWrapper(), SysDictTypeVo::class.java)
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Cacheable(cacheNames = [CacheNames.SYS_DICT], key = "#dictType")
    override fun selectDictDataByType(dictType: String): MutableList<SysDictDataVo>? {
        val dictDatas: MutableList<SysDictDataVo> = dictDataMapper.selectDictDataByType(dictType)
        return if (CollUtil.isNotEmpty(dictDatas)) {
            dictDatas
        } else null
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    override fun selectDictTypeById(dictId: Long): SysDictTypeVo? {
        return baseMapper.selectOneWithRelationsByIdAs(dictId, SysDictTypeVo::class.java)
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    override fun selectDictTypeByType(dictType: String): SysDictTypeVo {
        return baseMapper.selectOneByQueryAs(
            QueryWrapper.create().from(SYS_DICT_TYPE).where(SYS_DICT_TYPE.DICT_TYPE.eq(dictType)),
            SysDictTypeVo::class.java
        )
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    override fun deleteDictTypeByIds(dictIds: Array<Long>) {
        for (dictId in dictIds) {
            val dictType = baseMapper.selectOneById(dictId)
            if (dictDataMapper.selectCountByQuery(
                    QueryWrapper.create().from(SYS_DICT_TYPE).where(SYS_DICT_TYPE.DICT_TYPE.eq(dictType.dictType))
                ) > 0
            ) {
                throw ServiceException("${dictType.dictName}已分配,不能删除")
            }
            CacheUtils.evict(CacheNames.SYS_DICT, dictType.dictType)
        }
        baseMapper.deleteBatchByIds(listOf(*dictIds))
    }

    /**
     * 重置字典缓存数据
     */
    override fun resetDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT)
    }

    /**
     * 新增保存字典类型信息
     *
     * @param bo 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_DICT], key = "#bo.dictType")
    override fun insertDictType(bo: SysDictTypeBo): MutableList<SysDictDataVo> {
        val dict: SysDictType? = convert(bo, SysDictType::class.java)
        val row = baseMapper.insert(dict, true)
        if (row > 0) {
            // 新增 type 下无 data 数据 返回空防止缓存穿透
            return mutableListOf()
        }
        throw ServiceException("操作失败")
    }

    /**
     * 修改保存字典类型信息
     *
     * @param bo 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_DICT], key = "#bo.dictType")
    @Transactional(rollbackFor = [Exception::class])
    override fun updateDictType(bo: SysDictTypeBo): MutableList<SysDictDataVo> {
        val dict: SysDictType = convert(bo, SysDictType::class.java)!!
        val oldDict = baseMapper.selectOneById(dict.dictId)
        UpdateChain.of(SysDictData::class.java)
            .set(SYS_DICT_DATA.DICT_TYPE, dict.dictType)
            .where(SYS_DICT_DATA.DICT_TYPE.eq(oldDict.dictType))
            .update()
        val row = baseMapper.update(dict)
        if (row > 0) {
            CacheUtils.evict(CacheNames.SYS_DICT, oldDict.dictType)
            return dictDataMapper.selectDictDataByType(dict.dictType!!)
        }
        throw ServiceException("操作失败")
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    override fun checkDictTypeUnique(dictType: SysDictTypeBo): Boolean {
        val exist = baseMapper.selectCountByQuery(
            QueryWrapper.create().from(SYS_DICT_TYPE).where(SYS_DICT_TYPE.DICT_TYPE.eq(dictType.dictType))
                .and(SYS_DICT_TYPE.DICT_ID.ne(dictType.dictId))
        ) > 0
        return !exist
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    override fun getDictLabel(dictType: String, dictValue: String, separator: String): String {
        // 优先从本地缓存获取
        var datas = SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] as MutableList<SysDictDataVo>
        if (ObjectUtil.isNull(datas)) {
            datas = getAopProxy(this).selectDictDataByType(dictType)!!
            SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] = datas
        }
        val map: MutableMap<String, String> =
            toMap(datas, SysDictDataVo::dictValue, SysDictDataVo::dictLabel) as MutableMap<String, String>
        return if (StringUtils.containsAny(dictValue, separator)) {
            Arrays.stream(dictValue.split(separator.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .map { v: String ->
                    map[v] ?: StringUtils.EMPTY
                }
                .collect(Collectors.joining(separator))
        } else {
            map[dictValue] ?: StringUtils.EMPTY
        }
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param dictType  字典类型
     * @param dictLabel 字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    override fun getDictValue(dictType: String, dictLabel: String, separator: String): String {
        // 优先从本地缓存获取
        var datas = SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] as MutableList<SysDictDataVo>
        if (ObjectUtil.isNull(datas)) {
            datas = getAopProxy(this).selectDictDataByType(dictType)!!
            SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] = datas
        }
        val map: MutableMap<String, String> =
            toMap(datas, SysDictDataVo::dictLabel, SysDictDataVo::dictValue) as MutableMap<String, String>
        return if (StringUtils.containsAny(dictLabel, separator)) {
            Arrays.stream(dictLabel.split(separator.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray())
                .map { l: String ->
                    map[l] ?: StringUtils.EMPTY
                }
                .collect(Collectors.joining(separator))
        } else {
            map[dictLabel] ?: StringUtils.EMPTY
        }
    }

    override fun getAllDictByDictType(dictType: String): MutableMap<String, String>? {
        val list = selectDictDataByType(dictType)!!
        return toMap(list, SysDictDataVo::dictValue, SysDictDataVo::dictLabel) as MutableMap<String, String>?
    }
}
