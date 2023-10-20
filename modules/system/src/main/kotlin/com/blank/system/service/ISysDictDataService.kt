package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysDictDataBo
import com.blank.system.domain.vo.SysDictDataVo

/**
 * 字典 业务层
 */
interface ISysDictDataService {
    fun selectPageDictDataList(dictData: SysDictDataBo, pageQuery: PageQuery): TableDataInfo<SysDictDataVo>

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    fun selectDictDataList(dictData: SysDictDataBo): MutableList<SysDictDataVo>

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    fun selectDictLabel(dictType: String, dictValue: String): String?

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    fun selectDictDataById(dictCode: Long): SysDictDataVo?

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    fun deleteDictDataByIds(dictCodes: Array<Long>)

    /**
     * 新增保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    fun insertDictData(bo: SysDictDataBo): MutableList<SysDictDataVo>

    /**
     * 修改保存字典数据信息
     *
     * @param bo 字典数据信息
     * @return 结果
     */
    fun updateDictData(bo: SysDictDataBo): MutableList<SysDictDataVo>
}
