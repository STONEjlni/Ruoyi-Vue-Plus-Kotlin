package com.blank.system.service.impl

import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysDictData
import com.blank.system.domain.bo.SysDictDataBo
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

    override fun selectPageDictDataList(dictData: SysDictDataBo, pageQuery: PageQuery): TableDataInfo<SysDictDataVo>? {
        /*LambdaQueryWrapper<SysDictData> lqw = buildQueryWrapper(dictData);
        Page<SysDictDataVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    override fun selectDictDataList(dictData: SysDictDataBo): MutableList<SysDictDataVo>? {
        /*LambdaQueryWrapper<SysDictData> lqw = buildQueryWrapper(dictData);
        return baseMapper.selectVoList(lqw);*/
        return null
    }

    private fun  /*<SysDictData>*/buildQueryWrapper(bo: SysDictDataBo): QueryWrapper? {
        /*LambdaQueryWrapper<SysDictData> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getDictSort() != null, SysDictData::getDictSort, bo.getDictSort());
        lqw.like(StrUtil.isNotBlank(bo.getDictLabel()), SysDictData::getDictLabel, bo.getDictLabel());
        lqw.eq(StrUtil.isNotBlank(bo.getDictType()), SysDictData::getDictType, bo.getDictType());
        lqw.orderByAsc(SysDictData::getDictSort);
        return lqw;*/
        return null
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    override fun selectDictLabel(dictType: String, dictValue: String): String? {
        /*return baseMapper.selectOne(new LambdaQueryWrapper<SysDictData>()
                .select(SysDictData::getDictLabel)
                .eq(SysDictData::getDictType, dictType)
                .eq(SysDictData::getDictValue, dictValue))
            .getDictLabel();*/
        return null
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    override fun selectDictDataById(dictCode: Long): SysDictDataVo? {
        return baseMapper!!.selectVoById(dictCode)!!
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    override fun deleteDictDataByIds(dictCodes: Array<Long>) {
        /*for (Long dictCode : dictCodes) {
            SysDictData data = baseMapper.selectById(dictCode);
            baseMapper.deleteById(dictCode);
            CacheUtils.evict(CacheNames.SYS_DICT, data.getDictType());
        }*/
    }

    /**
     * 新增保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_DICT], key = "#bo.dictType")
    override fun insertDictData(bo: SysDictDataBo): MutableList<SysDictDataVo>? {
        val data = convert(bo, SysDictData::class.java)!!
        val row = baseMapper!!.insert(data)
        if (row > 0) {
            return baseMapper.selectDictDataByType(data.dictType)
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
    override fun updateDictData(bo: SysDictDataBo): MutableList<SysDictDataVo>? {
        /*SysDictData data = MapstructUtils.convert(bo, SysDictData.class);
        int row = baseMapper.updateById(data);
        if (row > 0) {
            return baseMapper.selectDictDataByType(data.getDictType());
        }
        throw new ServiceException("操作失败");*/
        return null
    }
}
