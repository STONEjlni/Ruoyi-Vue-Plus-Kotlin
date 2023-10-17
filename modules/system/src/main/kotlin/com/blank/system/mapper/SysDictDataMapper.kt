package com.blank.system.mapper

import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysDictData
import com.blank.system.domain.vo.SysDictDataVo
import com.mybatisflex.core.query.QueryWrapper

/**
 * 字典表 数据层
 */
interface SysDictDataMapper : BaseMapperPlus<SysDictData, SysDictDataVo> {
    fun selectDictDataByType(dictType: String?): MutableList<SysDictDataVo>? {
        return selectVoList(
            QueryWrapper.create().select().from(SysDictData::class.java) /*.and(SysDictData::getDictType, dictType)*/
                .orderBy { obj: SysDictData -> obj.dictSort }.asc()
        )
    }
}
