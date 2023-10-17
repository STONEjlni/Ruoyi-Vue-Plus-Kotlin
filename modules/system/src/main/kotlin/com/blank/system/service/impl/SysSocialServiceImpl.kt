package com.blank.system.service.impl

import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.system.domain.SysSocial
import com.blank.system.domain.bo.SysSocialBo
import com.blank.system.domain.vo.SysSocialVo
import com.blank.system.mapper.SysSocialMapper
import com.blank.system.service.ISysSocialService
import org.springframework.stereotype.Service

/**
 * 社会化关系Service业务层处理
 */
@Service
class SysSocialServiceImpl(
    private val baseMapper: SysSocialMapper
) : ISysSocialService {


    /**
     * 查询社会化关系
     */
    override fun queryById(id: String): SysSocialVo? {
        return baseMapper.selectVoById(id)
    }

    /**
     * 授权列表
     */
    override fun queryList(): MutableList<SysSocialVo>? {
        return baseMapper.selectVoList()
    }

    override fun queryListByUserId(userId: Long): MutableList<SysSocialVo>? {
        /*return baseMapper.selectVoList(new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getUserId, userId));*/
        return null
    }

    /**
     * 新增社会化关系
     */
    override fun insertByBo(bo: SysSocialBo): Boolean {
        val add = convert(bo, SysSocial::class.java)!!
        validEntityBeforeSave(add)
        val flag = baseMapper.insert(add) > 0
        if (flag) {
            if (add != null) {
                bo.id = add.id
            } else {
                return false
            }
        }
        return flag
    }

    /**
     * 更新社会化关系
     */
    override fun updateByBo(bo: SysSocialBo): Boolean {
        /*SysSocial update = MapstructUtils.convert(bo, SysSocial.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;*/
        return false
    }

    /**
     * 保存前的数据校验
     */
    private fun validEntityBeforeSave(entity: SysSocial) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 删除社会化关系
     */
    override fun deleteWithValidById(id: Long): Boolean {
        return baseMapper.deleteById(id) > 0
    }

    /**
     * 根据 authId 查询用户信息
     *
     * @param authId 认证id
     * @return 授权信息
     */
    override fun selectByAuthId(authId: String): SysSocialVo? {
        /*return baseMapper.selectVoOne(new LambdaQueryWrapper<SysSocial>().eq(SysSocial::getAuthId, authId));*/
        return null
    }
}
