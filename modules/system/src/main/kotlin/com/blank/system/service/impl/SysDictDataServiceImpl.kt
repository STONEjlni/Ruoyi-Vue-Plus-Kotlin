package com.blank.system.service.impl

import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.redis.utils.CacheUtils
import com.blank.system.domain.SysDictData
import com.blank.system.domain.bo.SysDictDataBo
import com.blank.system.domain.table.SysDictDataDef.SYS_DICT_DATA
import com.blank.system.domain.vo.SysDictDataVo
import com.blank.system.mapper.SysDictDataMapper
import com.blank.system.service.ISysDictDataService
import com.mybatisflex.core.query.QueryWrapper
import org.springframework.cache.annotation.CachePut
import org.springframework.stereotype.Service

/**
 * 字典 业务层处理
 */
@Service
class SysDictDataServiceImpl(
    private val baseMapper: SysDictDataMapper
) : ISysDictDataService {

    override fun selectPageDictDataList(dictData: SysDictDataBo, pageQuery: PageQuery): TableDataInfo<SysDictDataVo> {
        val lqw = buildQueryWrapper(dictData)
        val page = baseMapper.paginateAs(pageQuery, lqw, SysDictDataVo::class.java)
        return TableDataInfo.build(page)
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    override fun selectDictDataList(dictData: SysDictDataBo): MutableList<SysDictDataVo> {
        val lqw = buildQueryWrapper(dictData)
        return baseMapper.selectListByQueryAs(lqw, SysDictDataVo::class.java)
    }

    private fun buildQueryWrapper(bo: SysDictDataBo): QueryWrapper {
        return QueryWrapper.create()
            .from(SYS_DICT_DATA)
            .where(SYS_DICT_DATA.DICT_SORT.eq(bo.dictSort))
            .and(SYS_DICT_DATA.DICT_LABEL.like(bo.dictLabel))
            .and(SYS_DICT_DATA.DICT_TYPE.eq(bo.dictType))
            .orderBy(SYS_DICT_DATA.DICT_SORT, true)
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    override fun selectDictLabel(dictType: String, dictValue: String): String? {
        return baseMapper.selectOneByQuery(
            QueryWrapper.create()
                .select(SYS_DICT_DATA.DICT_LABEL)
                .from(SYS_DICT_DATA)
                .where(SYS_DICT_DATA.DICT_TYPE.eq(dictType))
                .and(SYS_DICT_DATA.DICT_VALUE.eq(dictValue))
        ).dictLabel
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    override fun selectDictDataById(dictCode: Long): SysDictDataVo? {
        return baseMapper.selectOneWithRelationsByIdAs(dictCode, SysDictDataVo::class.java)
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    override fun deleteDictDataByIds(dictCodes: Array<Long>) {
        for (dictCode in dictCodes) {
            val `data` = baseMapper.selectOneById(dictCode)
            baseMapper.deleteById(dictCode)
            CacheUtils.evict(CacheNames.SYS_DICT, `data`.dictType)
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_DICT], key = "#bo.dictType")
    override fun insertDictData(bo: SysDictDataBo): MutableList<SysDictDataVo> {
        val `data`: SysDictData? = convert(bo, SysDictData::class.java)
        val row = baseMapper.insert(`data`, true)
        if (row > 0) {
            return baseMapper.selectDictDataByType(`data`?.dictType!!)
        }
        throw ServiceException("操作失败")
    }

    /**
     * 修改保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_DICT], key = "#bo.dictType")
    override fun updateDictData(bo: SysDictDataBo): MutableList<SysDictDataVo> {
        val `data`: SysDictData? = convert(bo, SysDictData::class.java)
        val row = baseMapper.update(`data`)
        if (row > 0) {
            return baseMapper.selectDictDataByType(`data`?.dictType!!)
        }
        throw ServiceException("操作失败")
    }
}
