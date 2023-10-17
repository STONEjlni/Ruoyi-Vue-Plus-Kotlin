package com.blank.system.service.impl

import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.service.ConfigService
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.core.utils.SpringUtilExtend.getAopProxy
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.redis.utils.CacheUtils.clear
import com.blank.system.domain.SysConfig
import com.blank.system.domain.bo.SysConfigBo
import com.blank.system.domain.vo.SysConfigVo
import com.blank.system.mapper.SysConfigMapper
import com.blank.system.service.ISysConfigService
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

/**
 * 参数配置 服务层实现
 */
@Service
class SysConfigServiceImpl : ISysConfigService, ConfigService {
    private val baseMapper: SysConfigMapper? = null
    override fun selectPageConfigList(config: SysConfigBo, pageQuery: PageQuery): TableDataInfo<SysConfigVo>? {
        /*LambdaQueryWrapper<SysConfig> lqw = buildQueryWrapper(config);
        Page<SysConfigVo> page = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);*/
        return null
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    override fun selectConfigById(configId: Long): SysConfigVo {
        return baseMapper!!.selectVoById(configId)!!
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = [CacheNames.SYS_CONFIG], key = "#configKey")
    override fun selectConfigByKey(configKey: String): String {
        /*SysConfig retConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
            .eq(SysConfig::getConfigKey, configKey));
        if (ObjectUtil.isNotNull(retConfig)) {
            return retConfig.getConfigValue();
        }
        return StringUtils.EMPTY;*/
        return StringUtils.EMPTY
    }

    /**
     * 获取注册开关
     *
     * @param tenantId 租户id
     * @return true开启，false关闭
     */
    override fun selectRegisterEnabled(tenantId: String): Boolean {
        /*SysConfig retConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
            .eq(SysConfig::getConfigKey, "sys.account.registerUser")
            .eq(TenantHelper.isEnable(),SysConfig::getTenantId, tenantId));
        if (ObjectUtil.isNull(retConfig)) {
            return false;
        }
        return Convert.toBool(retConfig.getConfigValue());*/
        return false
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    override fun selectConfigList(config: SysConfigBo): MutableList<SysConfigVo>? {
        /*LambdaQueryWrapper<SysConfig> lqw = buildQueryWrapper(config);
        return baseMapper.selectVoList(lqw);*/
        return null
    }

    private fun  /*<SysConfig>*/buildQueryWrapper(bo: SysConfigBo): QueryWrapper? {
        /*Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<SysConfig> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotBlank(bo.getConfigName()), SysConfig::getConfigName, bo.getConfigName());
        lqw.eq(StrUtil.isNotBlank(bo.getConfigType()), SysConfig::getConfigType, bo.getConfigType());
        lqw.like(StrUtil.isNotBlank(bo.getConfigKey()), SysConfig::getConfigKey, bo.getConfigKey());
        lqw.between(params.get("beginTime") != null && params.get("endTime") != null,
            SysConfig::getCreateTime, params.get("beginTime"), params.get("endTime"));
        lqw.orderByAsc(SysConfig::getConfigId);
        return lqw;*/
        return null
    }

    /**
     * 新增参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = [CacheNames.SYS_CONFIG], key = "#bo.configKey")
    override fun insertConfig(bo: SysConfigBo): String? {
        val config = convert(bo, SysConfig::class.java)!!
        val row = baseMapper!!.insert(config)
        if (row > 0) {
            return config.configValue
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
        /*int row = 0;
        SysConfig config = MapstructUtils.convert(bo, SysConfig.class);
        if (config.getConfigId() != null) {
            SysConfig temp = baseMapper.selectById(config.getConfigId());
            if (!StringUtils.equals(temp.getConfigKey(), config.getConfigKey())) {
                CacheUtils.evict(CacheNames.SYS_CONFIG, temp.getConfigKey());
            }
            row = baseMapper.updateById(config);
        } else {
            row = baseMapper.update(config, new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getConfigKey, config.getConfigKey()));
        }
        if (row > 0) {
            return config.getConfigValue();
        }
        throw new ServiceException("操作失败");*/
        return null
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    override fun deleteConfigByIds(configIds: Array<Long>) {
        /*for (Long configId : configIds) {
            SysConfig config = baseMapper.selectById(configId);
            if (StringUtils.equals(UserConstants.YES, config.getConfigType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getConfigKey()));
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, config.getConfigKey());
        }
        baseMapper.deleteBatchIds(Arrays.asList(configIds));*/
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
        /*long configId = ObjectUtil.isNull(config.getConfigId()) ? -1L : config.getConfigId();
        SysConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getConfigKey, config.getConfigKey()));
        if (ObjectUtil.isNotNull(info) && info.getConfigId() != configId) {
            return false;
        }
        return true;*/
        return false
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param configKey 参数 key
     * @return 参数值
     */
    override fun getConfigValue(configKey: String): String? {
        return getAopProxy(this).selectConfigByKey(configKey)
    }
}
