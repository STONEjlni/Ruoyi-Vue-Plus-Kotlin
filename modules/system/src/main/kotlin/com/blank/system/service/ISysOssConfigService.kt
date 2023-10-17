package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysOssConfigBo
import com.blank.system.domain.vo.SysOssConfigVo

/**
 * 对象存储配置Service接口
 */
interface ISysOssConfigService {
    /**
     * 初始化OSS配置
     */
    fun init()

    /**
     * 查询单个
     */
    fun queryById(ossConfigId: Long): SysOssConfigVo?

    /**
     * 查询列表
     */
    fun queryPageList(bo: SysOssConfigBo, pageQuery: PageQuery): TableDataInfo<SysOssConfigVo>?

    /**
     * 根据新增业务对象插入对象存储配置
     *
     * @param bo 对象存储配置新增业务对象
     * @return
     */
    fun insertByBo(bo: SysOssConfigBo): Boolean

    /**
     * 根据编辑业务对象修改对象存储配置
     *
     * @param bo 对象存储配置编辑业务对象
     * @return
     */
    fun updateByBo(bo: SysOssConfigBo): Boolean

    /**
     * 校验并删除数据
     *
     * @param ids     主键集合
     * @param isValid 是否校验,true-删除前校验,false-不校验
     * @return
     */
    fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean

    /**
     * 启用停用状态
     */
    fun updateOssConfigStatus(bo: SysOssConfigBo): Int
}
