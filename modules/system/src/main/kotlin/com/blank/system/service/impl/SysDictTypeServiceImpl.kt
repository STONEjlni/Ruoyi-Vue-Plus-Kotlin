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
import com.blank.common.redis.utils.CacheUtils.clear
import com.blank.system.domain.SysDictType
import com.blank.system.domain.bo.SysDictTypeBo
import com.blank.system.domain.vo.SysDictDataVo
import com.blank.system.domain.vo.SysDictTypeVo
import com.blank.system.mapper.SysDictDataMapper
import com.blank.system.mapper.SysDictTypeMapper
import com.blank.system.service.ISysDictTypeService
import com.mybatisflex.core.query.QueryWrapper
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

    override fun selectPageDictTypeList(dictType: SysDictTypeBo, pageQuery: PageQuery): TableDataInfo<SysDictTypeVo>? {
        /*LambdaQueryWrapper<SysDictType> lqw = buildQueryWrapper(dictType);
        Page<SysDictTypeVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    override fun selectDictTypeList(dictType: SysDictTypeBo): MutableList<SysDictTypeVo>? {
        /*LambdaQueryWrapper<SysDictType> lqw = buildQueryWrapper(dictType);
        return baseMapper.selectVoList(lqw);*/
        return null
    }

    private fun  /*<SysDictType>*/buildQueryWrapper(bo: SysDictTypeBo): QueryWrapper? {
        /*Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysDictType> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getDictName()), SysDictType::getDictName, bo.getDictName());
        lqw.like(StrUtil.isNotBlank(bo.getDictType()), SysDictType::getDictType, bo.getDictType());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            SysDictType::getCreateTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByAsc(SysDictType::getDictId);
        return lqw;*/
        return null
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    override fun selectDictTypeAll(): MutableList<SysDictTypeVo>? {
        return baseMapper.selectVoList()
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Cacheable(cacheNames = [CacheNames.SYS_DICT], key = "#dictType")
    override fun selectDictDataByType(dictType: String): MutableList<SysDictDataVo>? {
        val dictDatas = dictDataMapper.selectDictDataByType(dictType)
        return if (CollUtil.isNotEmpty(dictDatas)) {
            dictDatas?.toMutableList()
        } else null
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    override fun selectDictTypeById(dictId: Long): SysDictTypeVo? {
        return baseMapper!!.selectVoById(dictId)!!
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    override fun selectDictTypeByType(dictType: String): SysDictTypeVo? {
        /*return baseMapper.selectVoById(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getDictType, dictType));*/
        return null
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    override fun deleteDictTypeByIds(dictIds: Array<Long>) {
        /*for (Long dictId : dictIds) {
            SysDictType dictType = baseMapper.selectById(dictId);
            if (dictDataMapper.exists(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getDictType, dictType.getDictType()))) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getDictName()));
            }
            CacheUtils.evict(CacheNames.SYS_DICT, dictType.getDictType());
        }
        baseMapper.deleteBatchIds(Arrays.asList(dictIds));*/
    }

    /**
     * 重置字典缓存数据
     */
    override fun resetDictCache() {
        clear(CacheNames.SYS_DICT)
    }

    /**
     * 新增保存字典类型信息
     *
     * @param bo 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_DICT], key = "#bo.dictType")
    override fun insertDictType(bo: SysDictTypeBo): MutableList<SysDictDataVo>? {
        val dict = convert(bo, SysDictType::class.java)!!
        val row = baseMapper!!.insert(dict)
        if (row > 0) {
            // 新增 type 下无 data 数据 返回空防止缓存穿透
            return ArrayList()
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
    override fun updateDictType(bo: SysDictTypeBo): MutableList<SysDictDataVo>? {
        /*SysDictType dict = MapstructUtils.convert(bo, SysDictType.class);
        SysDictType oldDict = baseMapper.selectById(dict.getDictId());
        dictDataMapper.update(null, new LambdaUpdateWrapper<SysDictData>()
            .set(SysDictData::getDictType, dict.getDictType())
            .eq(SysDictData::getDictType, oldDict.getDictType()));
        int row = baseMapper.updateById(dict);
        if (row > 0) {
            CacheUtils.evict(CacheNames.SYS_DICT, oldDict.getDictType());
            return dictDataMapper.selectDictDataByType(dict.getDictType());
        }
        throw new ServiceException("操作失败");*/
        return null
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    override fun checkDictTypeUnique(dictType: SysDictTypeBo): Boolean {
        /*boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDictType>()
            .eq(SysDictType::getDictType, dictType.getDictType())
            .ne(ObjectUtil.isNotNull(dictType.getDictId()), SysDictType::getDictId, dictType.getDictId()));
        return !exist;*/
        return false
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    override fun getDictLabel(dictType: String, dictValue: String, separator: String): String? {
        // 优先从本地缓存获取
        var datas = SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] as MutableList<SysDictDataVo>
        if (ObjectUtil.isNull(datas)) {
            datas = getAopProxy(this).selectDictDataByType(dictType)!!
            SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] = datas
        }
        val map = toMap(datas, { obj: SysDictDataVo -> obj.dictValue }) { obj: SysDictDataVo -> obj.dictLabel }
        return if (StringUtils.containsAny(dictValue, separator)) {
            Arrays.stream(dictValue.split(separator).toTypedArray())
                .map { v: String? -> map[v] ?: StringUtils.EMPTY }
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
    override fun getDictValue(dictType: String, dictLabel: String, separator: String): String? {
        // 优先从本地缓存获取
        var datas = SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] as MutableList<SysDictDataVo>
        if (ObjectUtil.isNull(datas)) {
            datas = getAopProxy(this).selectDictDataByType(dictType)!!
            SaHolder.getStorage()[CacheConstants.SYS_DICT_KEY + dictType] = datas
        }
        val map = toMap(datas, { obj: SysDictDataVo -> obj.dictLabel }) { obj: SysDictDataVo -> obj.dictValue }
        return if (StringUtils.containsAny(dictLabel, separator)) {
            Arrays.stream(dictLabel!!.split(separator).toTypedArray())
                .map { l: String? -> map[l] ?: StringUtils.EMPTY }
                .collect(Collectors.joining(separator))
        } else {
            map[dictLabel] ?: StringUtils.EMPTY
        }
    }

    override fun getAllDictByDictType(dictType: String): Map<String, String>? {
        val list = selectDictDataByType(dictType)!!
        return toMap(list, { obj: SysDictDataVo -> obj.dictValue!! }) { obj: SysDictDataVo -> obj.dictLabel!! }
    }
}
