package com.blank.system.mapper

import com.blank.common.mybatis.core.mapper.BaseMapperPlus
import com.blank.system.domain.SysDictData
import com.blank.system.domain.table.SysDictDataDef.SYS_DICT_DATA
import com.blank.system.domain.vo.SysDictDataVo
import com.mybatisflex.core.query.QueryWrapper

/**
 * 字典表 数据层
 */
interface SysDictDataMapper : BaseMapperPlus<SysDictData> {
    fun selectDictDataByType(dictType: String): MutableList<SysDictDataVo> {
        return selectListByQueryAs(
            QueryWrapper.create().select().from(SYS_DICT_DATA)
                .where(SYS_DICT_DATA.DICT_TYPE.eq(dictType))
                .orderBy(SYS_DICT_DATA.DICT_SORT, true),
            SysDictDataVo::class.java
        )
    }
}
