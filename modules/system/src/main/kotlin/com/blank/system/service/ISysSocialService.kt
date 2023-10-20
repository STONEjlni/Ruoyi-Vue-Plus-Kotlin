package com.blank.system.service

import com.blank.system.domain.bo.SysSocialBo
import com.blank.system.domain.vo.SysSocialVo

/**
 * 社会化关系Service接口
 */
interface ISysSocialService {
    /**
     * 查询社会化关系
     */
    fun queryById(id: String): SysSocialVo?

    /**
     * 查询社会化关系列表
     */
    fun queryList(): MutableList<SysSocialVo>

    /**
     * 查询社会化关系列表
     */
    fun queryListByUserId(userId: Long): MutableList<SysSocialVo>

    /**
     * 新增授权关系
     */
    fun insertByBo(bo: SysSocialBo): Boolean

    /**
     * 更新社会化关系
     */
    fun updateByBo(bo: SysSocialBo): Boolean

    /**
     * 删除社会化关系信息
     */
    fun deleteWithValidById(id: Long): Boolean

    /**
     * 根据 authId 查询 SysSocial 表和 SysUser 表，返回 SysSocialAuthResult 映射的对象
     *
     * @param authId 认证ID
     * @return SysSocial
     */
    fun selectByAuthId(authId: String): SysSocialVo?
}
