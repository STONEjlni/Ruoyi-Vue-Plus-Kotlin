package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysDictTypeBo
import com.blank.system.domain.vo.SysDictDataVo
import com.blank.system.domain.vo.SysDictTypeVo

/**
 * 字典 业务层
 */
interface ISysDictTypeService {
    fun selectPageDictTypeList(dictType: SysDictTypeBo, pageQuery: PageQuery): TableDataInfo<SysDictTypeVo>

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    fun selectDictTypeList(dictType: SysDictTypeBo): MutableList<SysDictTypeVo>

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    fun selectDictTypeAll(): MutableList<SysDictTypeVo>

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    fun selectDictDataByType(dictType: String): MutableList<SysDictDataVo>?

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    fun selectDictTypeById(dictId: Long): SysDictTypeVo?

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    fun selectDictTypeByType(dictType: String): SysDictTypeVo?

    /**
     * 批量删除字典信息
     *
     * @param dictIds 需要删除的字典ID
     */
    fun deleteDictTypeByIds(dictIds: Array<Long>)

    /**
     * 重置字典缓存数据
     */
    fun resetDictCache()

    /**
     * 新增保存字典类型信息
     *
     * @param bo 字典类型信息
     * @return 结果
     */
    fun insertDictType(bo: SysDictTypeBo): MutableList<SysDictDataVo>

    /**
     * 修改保存字典类型信息
     *
     * @param bo 字典类型信息
     * @return 结果
     */
    fun updateDictType(bo: SysDictTypeBo): MutableList<SysDictDataVo>

    /**
     * 校验字典类型称是否唯一
     *
     * @param dictType 字典类型
     * @return 结果
     */
    fun checkDictTypeUnique(dictType: SysDictTypeBo): Boolean
}
