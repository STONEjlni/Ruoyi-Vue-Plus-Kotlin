package com.blank.system.service.impl

import cn.hutool.core.convert.Convert
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.constant.UserConstants
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.ConfigService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.SpringUtilExtend
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.redis.utils.CacheUtils
import com.blank.common.redis.utils.CacheUtils.clear
import com.blank.system.domain.SysConfig
import com.blank.system.domain.bo.SysConfigBo
import com.blank.system.domain.table.SysConfigDef.SYS_CONFIG
import com.blank.system.domain.vo.SysConfigVo
import com.blank.system.mapper.SysConfigMapper
import com.blank.system.service.ISysConfigService
import com.mybatisflex.annotation.UseDataSource
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 参数配置 服务层实现
 */
@Service
class SysConfigServiceImpl(
    private val baseMapper: SysConfigMapper
) : ISysConfigService, ConfigService {

    override fun selectPageConfigList(config: SysConfigBo, pageQuery: PageQuery): TableDataInfo<SysConfigVo> {
        val lqw = buildQueryWrapper(config)
        val page = baseMapper.paginateAs(pageQuery, lqw, SysConfigVo::class.java)
        return TableDataInfo.build(page)
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @UseDataSource("master")
    override fun selectConfigById(configId: Long): SysConfigVo? {
        return baseMapper.selectOneWithRelationsByIdAs(configId, SysConfigVo::class.java)
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = [CacheNames.SYS_CONFIG], key = "#configKey")
    override fun selectConfigByKey(configKey: String): String? {
        val retConfig = baseMapper.selectOneByQuery(
            QueryWrapper.create().from(SYS_CONFIG)
                .where(SYS_CONFIG.CONFIG_KEY.eq(configKey))
        )
        return if (ObjectUtil.isNotNull(retConfig)) {
            retConfig.configValue
        } else StringUtils.EMPTY
    }

    /**
     * 获取注册开关
     *
     * @param tenantId 租户id
     * @return true开启，false关闭
     */
    override fun selectRegisterEnabled(tenantId: String): Boolean {
        val retConfig = baseMapper.selectOneByQuery(
            QueryWrapper.create().from(SYS_CONFIG)
                .where(SYS_CONFIG.CONFIG_KEY.eq("sys.account.registerUser"))
        )
        return if (ObjectUtil.isNull(retConfig)) {
            false
        } else Convert.toBool(retConfig.configValue)
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    override fun selectConfigList(config: SysConfigBo): MutableList<SysConfigVo> {
        val lqw = buildQueryWrapper(config)
        return baseMapper.selectListByQueryAs(lqw, SysConfigVo::class.java)
    }

    private fun buildQueryWrapper(bo: SysConfigBo): QueryWrapper {
        val params: MutableMap<String, Any> = bo.params
        return QueryWrapper.create().from(SYS_CONFIG)
            .where(SYS_CONFIG.CONFIG_NAME.like(bo.configName))
            .and(SYS_CONFIG.CONFIG_TYPE.eq(bo.configType))
            .where(SYS_CONFIG.CONFIG_KEY.like(bo.configKey))
            .and(
                SYS_CONFIG.CREATE_TIME.between(
                    params["beginTime"],
                    params["endTime"],
                    params["beginTime"] != null && params["endTime"] != null
                )
            )
            .orderBy(SYS_CONFIG.CONFIG_ID, true)
    }

    /**
     * 新增参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_CONFIG], key = "#bo.configKey")
    override fun insertConfig(bo: SysConfigBo): String? {
        val config: SysConfig? = convert(bo, SysConfig::class.java)
        val row = baseMapper.insert(config, true)
        if (row > 0) {
            return config?.configValue
        }
        throw ServiceException("操作失败")
    }

    /**
     * 修改参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_CONFIG], key = "#bo.configKey")
    override fun updateConfig(bo: SysConfigBo): String? {
        val config: SysConfig? = convert(bo, SysConfig::class.java)
        val row = if (config?.configId != null) {
            val temp = baseMapper.selectOneById(config.configId)
            if (!StringUtils.equals(temp.configKey, config.configKey)) {
                CacheUtils.evict(CacheNames.SYS_CONFIG, temp.configKey)
            }
            baseMapper.update(config)
        } else {
            baseMapper.updateByQuery(
                config,
                QueryWrapper.create().from(SYS_CONFIG).where(SYS_CONFIG.CONFIG_KEY.eq(config?.configKey))
            )
        }
        if (row > 0) {
            return config?.configValue
        }
        throw ServiceException("操作失败")
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    override fun deleteConfigByIds(configIds: Array<Long>) {
        for (configId in configIds) {
            val config = baseMapper.selectOneById(configId)
            if (StringUtils.equals(UserConstants.YES, config.configType)) {
                throw ServiceException(java.lang.String.format("内置参数【%1\$s】不能删除 ", config.configKey))
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, config.configKey)
        }
        baseMapper.deleteBatchByIds(listOf(*configIds))
    }

    /**
     * 重置参数缓存数据
     */
    override fun resetConfigCache() {
        clear(CacheNames.SYS_CONFIG)
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    override fun checkConfigKeyUnique(config: SysConfigBo): Boolean {
        val configId = if (ObjectUtil.isNull(config.configId)) -1L else config.configId
        val info = baseMapper.selectOneByQuery(
            QueryWrapper.create().from(SYS_CONFIG).where(SYS_CONFIG.CONFIG_KEY.eq(config.configKey))
        )
        return !ObjectUtil.isNotNull(info) || info.configId === configId
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    override fun getConfigValue(configKey: String): String? {
        return SpringUtilExtend.getAopProxy(this).selectConfigByKey(configKey)
    }
}
