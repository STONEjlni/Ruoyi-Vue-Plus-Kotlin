package com.blank.system.service.impl

import cn.hutool.crypto.SecureUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.SysClient
import com.blank.system.domain.bo.SysClientBo
import com.blank.system.domain.vo.SysClientVo
import com.blank.system.mapper.SysClientMapper
import com.blank.system.service.ISysClientService
import com.mybatisflex.core.query.QueryWrapper
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
        val vo = baseMapper.selectVoById(id)!!
        vo.grantTypeList = vo.grantType!!.split(",".toRegex()).dropLastWhile { it.isEmpty() }.toMutableList()
        return vo
    }

    /**
     * 查询客户端管理
     */
    override fun queryByClientId(clientId: String): SysClient? {
//        return baseMapper.selectOne(new LambdaQueryWrapper<SysClient>().eq(SysClient::getClientId, clientId));
        return null
    }

    /**
     * 查询客户端管理列表
     */
    override fun queryPageList(bo: SysClientBo, pageQuery: PageQuery): TableDataInfo<SysClientVo>? {
        /*LambdaQueryWrapper<SysClient> lqw = buildQueryWrapper(bo);
        Page<SysClientVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        result.getRecords().forEach(r -> r.setGrantTypeList(List.of(r.getGrantType().split(","))));*/
        return null // TableDataInfo.build(result);
    }

    /**
     * 查询客户端管理列表
     */
    override fun queryList(bo: SysClientBo): MutableList<SysClientVo>? {
        /*LambdaQueryWrapper<SysClient> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);*/
        return null
    }

    private fun  /*<SysClient>*/buildQueryWrapper(bo: SysClientBo): QueryWrapper? {
        /*LambdaQueryWrapper<SysClient> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getClientId()), SysClient::getClientId, bo.getClientId());
        lqw.eq(StrUtil.isNotBlank(bo.getClientKey()), SysClient::getClientKey, bo.getClientKey());
        lqw.eq(StrUtil.isNotBlank(bo.getClientSecret()), SysClient::getClientSecret, bo.getClientSecret());
        lqw.eq(StrUtil.isNotBlank(bo.getStatus()), SysClient::getStatus, bo.getStatus());
        lqw.orderByAsc(SysClient::getId);*/
        return null
    }

    /**
     * 新增客户端管理
     */
    override fun insertByBo(bo: SysClientBo): Boolean {
        val add = convert(bo, SysClient::class.java)!!
        validEntityBeforeSave(add)
        add.grantType = java.lang.String.join(",", bo.grantTypeList)
        // 生成clientid
        val clientKey = bo.clientKey
        val clientSecret = bo.clientSecret
        add.clientId = SecureUtil.md5(clientKey + clientSecret)
        val flag = baseMapper.insert(add) > 0
        if (flag) {
            bo.id = add.id
        }
        return flag
    }

    /**
     * 修改客户端管理
     */
    override fun updateByBo(bo: SysClientBo): Boolean {
        val update = convert(bo, SysClient::class.java)!!
        validEntityBeforeSave(update)
        update.grantType = java.lang.String.join(",", bo.grantTypeList)
        return baseMapper.update(update) > 0
    }

    /**
     * 修改状态
     */
    override fun updateUserStatus(id: Long, status: String): Int {
        /*return baseMapper.update(null,
            new LambdaUpdateWrapper<SysClient>()
                .set(SysClient::getStatus, status)
                .eq(SysClient::getId, id));*/
        return 0
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
        // return baseMapper.deleteBatchIds(ids) > 0;
        return false
    }
}
