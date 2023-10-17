package com.blank.system.service.impl

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.MapstructUtils
import com.blank.common.json.utils.JsonUtils.toJsonString
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.redis.utils.CacheUtils.put
import com.blank.system.domain.SysOssConfig
import com.blank.system.domain.bo.SysOssConfigBo
import com.blank.system.domain.vo.SysOssConfigVo
import com.blank.system.mapper.SysOssConfigMapper
import com.blank.system.service.ISysOssConfigService
import com.mybatisflex.core.query.QueryWrapper
import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 对象存储配置Service业务层处理
 */
@Slf4j
@Service
class SysOssConfigServiceImpl(
    private val baseMapper: SysOssConfigMapper
) : ISysOssConfigService {


    /**
     * 项目启动时，初始化参数到缓存，加载配置类
     */
    override fun init() {
        /*List<SysOssConfig> list = TenantHelper.ignore(() ->
            baseMapper.selectList(
                new LambdaQueryWrapper<SysOssConfig>().orderByAsc(TenantEntity::getTenantId))
        );
        Map<String, List<SysOssConfig>> map = StreamUtils.groupByKey(list, SysOssConfig::getTenantId);
        try {
            for (String tenantId : map.keySet()) {
                TenantHelper.setDynamic(tenantId);
                // 加载OSS初始化配置
                for (SysOssConfig config : map.get(tenantId)) {
                    String configKey = config.getConfigKey();
                    if ("0".equals(config.getStatus())) {
                        RedisUtils.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY, configKey);
                    }
                    CacheUtils.put(CacheNames.SYS_OSS_CONFIG, config.getConfigKey(), JsonUtils.toJsonString(config));
                }
            }
        } finally {
            TenantHelper.clearDynamic();
        }*/
    }

    override fun queryById(ossConfigId: Long): SysOssConfigVo? {
        return baseMapper.selectVoById(ossConfigId)
    }

    override fun queryPageList(bo: SysOssConfigBo, pageQuery: PageQuery): TableDataInfo<SysOssConfigVo>? {
        /*LambdaQueryWrapper<SysOssConfig> lqw = buildQueryWrapper(bo);
        Page<SysOssConfigVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);*/
        return null
    }

    private fun  /*<SysOssConfig>*/buildQueryWrapper(bo: SysOssConfigBo): QueryWrapper? {
        /*LambdaQueryWrapper<SysOssConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(StrUtil.isNotBlank(bo.getConfigKey()), SysOssConfig::getConfigKey, bo.getConfigKey());
        lqw.like(StrUtil.isNotBlank(bo.getBucketName()), SysOssConfig::getBucketName, bo.getBucketName());
        lqw.eq(StrUtil.isNotBlank(bo.getStatus()), SysOssConfig::getStatus, bo.getStatus());
        lqw.orderByAsc(SysOssConfig::getOssConfigId);
        return lqw;*/
        return null
    }

    override fun insertByBo(bo: SysOssConfigBo): Boolean {
        var config = MapstructUtils.convert(bo, SysOssConfig::class.java)!!
        validEntityBeforeSave(config)
        val flag = baseMapper.insert(config) > 0
        if (flag) {
            // 从数据库查询完整的数据做缓存
            config = baseMapper.selectOneById(config.ossConfigId)
            put(CacheNames.SYS_OSS_CONFIG, config.configKey, toJsonString(config))
        }
        return flag
    }

    override fun updateByBo(bo: SysOssConfigBo): Boolean {
        var config = MapstructUtils.convert(bo, SysOssConfig::class.java)!!
        validEntityBeforeSave(config)
        /*LambdaUpdateWrapper<SysOssConfig> luw = new LambdaUpdateWrapper<>();
        luw.set(ObjectUtil.isNull(config.getPrefix()), SysOssConfig::getPrefix, "");
        luw.set(ObjectUtil.isNull(config.getRegion()), SysOssConfig::getRegion, "");
        luw.set(ObjectUtil.isNull(config.getExt1()), SysOssConfig::getExt1, "");
        luw.set(ObjectUtil.isNull(config.getRemark()), SysOssConfig::getRemark, "");
        luw.eq(SysOssConfig::getOssConfigId, config.getOssConfigId());
        boolean flag = baseMapper.update(config, luw) > 0;
        if (flag) {
            // 从数据库查询完整的数据做缓存
            config = baseMapper.selectOneById(config.ossConfigId)
            CacheUtils.put(CacheNames.SYS_OSS_CONFIG, config.getConfigKey(), JsonUtils.toJsonString(config));
        }
        return flag*/
        return false
    }

    /**
     * 保存前的数据校验
     */
    private fun validEntityBeforeSave(entity: SysOssConfig) {
        if (StringUtils.isNotEmpty(entity.configKey)
            && !checkConfigKeyUnique(entity)
        ) {
            throw ServiceException("操作配置'" + entity.configKey + "'失败, 配置key已存在!")
        }
    }

    override fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean {
        /*if (isValid) {
            if (CollUtil.containsAny(ids, OssConstant.SYSTEM_DATA_IDS)) {
                throw new ServiceException("系统内置, 不可删除!");
            }
        }
        List<SysOssConfig> list = CollUtil.newArrayList();
        for (Long configId : ids) {
            SysOssConfig config = baseMapper.selectById(configId);
            list.add(config);
        }
        boolean flag = baseMapper.deleteBatchIds(ids) > 0;
        if (flag) {
            list.forEach(sysOssConfig ->
                CacheUtils.evict(CacheNames.SYS_OSS_CONFIG, sysOssConfig.getConfigKey()));
        }
        return flag;*/
        return false
    }

    /**
     * 判断configKey是否唯一
     */
    private fun checkConfigKeyUnique(sysOssConfig: SysOssConfig): Boolean {
        /*long ossConfigId = ObjectUtil.isNull(sysOssConfig.getOssConfigId()) ? -1L : sysOssConfig.getOssConfigId();
        SysOssConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysOssConfig>()
            .select(SysOssConfig::getOssConfigId, SysOssConfig::getConfigKey)
            .eq(SysOssConfig::getConfigKey, sysOssConfig.getConfigKey()));
        if (ObjectUtil.isNotNull(info) && info.getOssConfigId() != ossConfigId) {
            return false;
        }
        return true;*/
        return false
    }

    /**
     * 启用禁用状态
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateOssConfigStatus(bo: SysOssConfigBo): Int {
        /*SysOssConfig sysOssConfig = MapstructUtils.convert(bo, SysOssConfig.class);
        int row = baseMapper.update(null, new LambdaUpdateWrapper<SysOssConfig>()
            .set(SysOssConfig::getStatus, "1"));
        row += baseMapper.updateById(sysOssConfig);
        if (row > 0) {
            RedisUtils.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY, sysOssConfig.getConfigKey());
        }
        return row;*/
        return 0
    }
}
