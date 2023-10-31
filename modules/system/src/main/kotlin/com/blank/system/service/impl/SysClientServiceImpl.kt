package com.blank.system.service.impl

import cn.hutool.crypto.SecureUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysClient
import com.blank.system.domain.bo.SysClientBo
import com.blank.system.domain.table.SysClientDef.SYS_CLIENT
import com.blank.system.domain.vo.SysClientVo
import com.blank.system.mapper.SysClientMapper
import com.blank.system.service.ISysClientService
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.update.UpdateChain
import org.springframework.stereotype.Service

/**
 * 客户端管理Service业务层处理
 */
@Slf4j
@Service
class SysClientServiceImpl(
    private val baseMapper: SysClientMapper
) : ISysClientService {

    /**
     * 查询客户端管理
     */
    override fun queryById(id: Long): SysClientVo? {
        val vo = baseMapper.selectOneWithRelationsByIdAs(id, SysClientVo::class.java)
        vo.grantTypeList = vo.grantType?.split(",")?.toMutableList()
        return vo
    }


    /**
     * 查询客户端管理
     */
    override fun queryByClientId(clientId: String): SysClient? {
        return baseMapper.selectOneByQuery(
            QueryWrapper.create().from(SYS_CLIENT).where(SYS_CLIENT.CLIENT_ID.eq(clientId))
        )
    }

    /**
     * 查询客户端管理列表
     */
    override fun queryPageList(bo: SysClientBo, pageQuery: PageQuery): TableDataInfo<SysClientVo> {
        val lqw = buildQueryWrapper(bo)
        val result = baseMapper.paginateAs(pageQuery, lqw, SysClientVo::class.java)
        result.records.forEach { r: SysClientVo ->
            r.grantTypeList = r.grantType?.split(",")?.toMutableList()
        }
        return TableDataInfo.build(result)
    }

    /**
     * 查询客户端管理列表
     */
    override fun queryList(bo: SysClientBo): MutableList<SysClientVo> {
        val lqw = buildQueryWrapper(bo)
        return baseMapper.selectListByQueryAs(lqw, SysClientVo::class.java)
    }

    private fun buildQueryWrapper(bo: SysClientBo): QueryWrapper {
        return QueryWrapper.create()
            .from(SYS_CLIENT)
            .where(SYS_CLIENT.CLIENT_ID.eq(bo.clientId))
            .and(SYS_CLIENT.CLIENT_KEY.eq(bo.clientKey))
            .and(SYS_CLIENT.CLIENT_SECRET.eq(bo.clientSecret))
            .and(SYS_CLIENT.STATUS.eq(bo.status))
            .orderBy(SYS_CLIENT.ID, true)
    }

    /**
     * 新增客户端管理
     */
    override fun insertByBo(bo: SysClientBo): Boolean {
        val add: SysClient = convert(bo, SysClient::class.java)!!
        validEntityBeforeSave(add)
        add.grantType = bo.grantTypeList?.joinToString { it }
        // 生成clientid
        val clientKey: String? = bo.clientKey
        val clientSecret: String? = bo.clientSecret
        add.clientId = SecureUtil.md5(clientKey + clientSecret)
        val flag = baseMapper.insert(add, true) > 0
        if (flag) {
            bo.id = add.id
        }
        return flag
    }

    /**
     * 修改客户端管理
     */
    override fun updateByBo(bo: SysClientBo): Boolean {
        val update: SysClient = convert(bo, SysClient::class.java)!!
        validEntityBeforeSave(update)
        update.grantType = bo.grantTypeList?.joinToString { it }
        return baseMapper.update(update) > 0
    }

    /**
     * 修改状态
     */
    override fun updateUserStatus(id: Long, status: String): Boolean {
        return UpdateChain.of<Class<SysClient>>(SysClient::class.java)
            .set(SYS_CLIENT.STATUS, status)
            .from(SysClient::class.java)
            .where(SYS_CLIENT.ID.eq(id))
            .update()
    }

    /**
     * 保存前的数据校验
     */
    private fun validEntityBeforeSave(entity: SysClient) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除客户端管理
     */
    override fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean {
        if (isValid) {
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchByIds(ids) > 0
    }
}
