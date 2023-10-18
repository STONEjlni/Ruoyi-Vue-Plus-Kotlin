package com.blank.system.mapper

import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysDictData
import com.blank.system.domain.table.SysDictDataDef
import com.blank.system.domain.vo.SysDictDataVo
import com.mybatisflex.core.query.QueryWrapper

/**
 * 字典表 数据层
 */
interface SysDictDataMapper : BaseMapperPlus<SysDictData, SysDictDataVo> {
    fun selectDictDataByType(dictType: String): MutableList<SysDictDataVo>? {
        val def = SysDictDataDef.SYS_DICT_DATA
        return selectVoList(
            QueryWrapper.create().select()
                .where {
                    def.DICT_TYPE.eq(dictType)
                }
                .orderBy(def.DICT_SORT, true)
        )
    }
}
