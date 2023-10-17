package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysClient
import com.blank.system.domain.bo.SysClientBo
import com.blank.system.domain.vo.SysClientVo

/**
 * 客户端管理Service接口
 */
interface ISysClientService {
    /**
     * 查询客户端管理
     */
    fun queryById(id: Long): SysClientVo?

    /**
     * 查询客户端信息基于客户端id
     */
    fun queryByClientId(clientId: String): SysClient?

    /**
     * 查询客户端管理列表
     */
    fun queryPageList(bo: SysClientBo, pageQuery: PageQuery): TableDataInfo<SysClientVo>?

    /**
     * 查询客户端管理列表
     */
    fun queryList(bo: SysClientBo): MutableList<SysClientVo>?

    /**
     * 新增客户端管理
     */
    fun insertByBo(bo: SysClientBo): Boolean

    /**
     * 修改客户端管理
     */
    fun updateByBo(bo: SysClientBo): Boolean

    /**
     * 修改状态
     */
    fun updateUserStatus(id: Long, status: String): Int

    /**
     * 校验并批量删除客户端管理信息
     */
    fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean
}
