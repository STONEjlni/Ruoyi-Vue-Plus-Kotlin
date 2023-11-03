package com.blank.system.service.impl

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.constant.CacheNames
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.MapstructUtils
import com.blank.common.core.utils.StreamUtils
import com.blank.common.json.utils.JsonUtils.toJsonString
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.oss.constant.OssConstant
import com.blank.common.redis.utils.CacheUtils
import com.blank.common.redis.utils.CacheUtils.put
import com.blank.common.redis.utils.RedisUtils
import com.blank.system.domain.SysOssConfig
import com.blank.system.domain.bo.SysOssConfigBo
import com.blank.system.domain.table.SysOssConfigDef.SYS_OSS_CONFIG
import com.blank.system.domain.vo.SysOssConfigVo
import com.blank.system.mapper.SysOssConfigMapper
import com.blank.system.service.ISysOssConfigService
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.update.UpdateChain
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
        val list: MutableList<SysOssConfig> = baseMapper.selectListByQuery(
            QueryWrapper.create().from(SYS_OSS_CONFIG)
        )
        val map: Map<Long?, List<SysOssConfig>> = StreamUtils.groupByKey(list, SysOssConfig::ossConfigId)
        for (ossConfigId in map.keys) {
            // 加载OSS初始化配置
            for (config in map[ossConfigId]!!) {
                val configKey: String = config.configKey!!
                if ("0" == config.status) {
                    RedisUtils.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY, configKey)
                }
                put(CacheNames.SYS_OSS_CONFIG, config.configKey, toJsonString(config))
            }
        }
    }

    override fun queryById(ossConfigId: Long): SysOssConfigVo {
        return baseMapper.selectOneWithRelationsByIdAs(ossConfigId, SysOssConfigVo::class.java)
    }

    override fun queryPageList(bo: SysOssConfigBo, pageQuery: PageQuery): TableDataInfo<SysOssConfigVo> {
        val lqw = buildQueryWrapper(bo)
        val result = baseMapper.paginateAs(
            pageQuery, lqw,
            SysOssConfigVo::class.java
        )
        return TableDataInfo.build(result)
    }


    private fun buildQueryWrapper(bo: SysOssConfigBo): QueryWrapper {
        return QueryWrapper.create().from(SYS_OSS_CONFIG)
            .where(SYS_OSS_CONFIG.CONFIG_KEY.eq(bo.configKey))
            .and(SYS_OSS_CONFIG.BUCKET_NAME.like(bo.bucketName))
            .and(SYS_OSS_CONFIG.STATUS.eq(bo.status))
            .orderBy(SYS_OSS_CONFIG.OSS_CONFIG_ID, true)
    }

    override fun insertByBo(bo: SysOssConfigBo): Boolean {
        var config = MapstructUtils.convert(bo, SysOssConfig::class.java)!!
        validEntityBeforeSave(config)
        val flag = baseMapper.insert(config, true) > 0
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
        val update = baseMapper.update(config, false) != 0
        if (update) {
            // 从数据库查询完整的数据做缓存
            config = baseMapper.selectOneById(config.ossConfigId)
            put(CacheNames.SYS_OSS_CONFIG, config.configKey, toJsonString(config))
        }
        return update
    }

    /**
     * 保存前的数据校验
     */
    private fun validEntityBeforeSave(entity: SysOssConfig) {
        if (StringUtils.isNotEmpty(entity.configKey)
            && !checkConfigKeyUnique(entity)
        ) {
            throw ServiceException("操作配置'${entity.configKey}'失败, 配置key已存在!")
        }
    }

    override fun deleteWithValidByIds(ids: MutableCollection<Long>, isValid: Boolean): Boolean {
        if (isValid) {
            if (CollUtil.containsAny(ids, OssConstant.SYSTEM_DATA_IDS)) {
                throw ServiceException("系统内置, 不可删除!")
            }
        }
        val list: MutableList<SysOssConfig> = CollUtil.newArrayList()
        for (configId in ids) {
            val config = baseMapper.selectOneById(configId)
            list.add(config)
        }
        val flag = baseMapper.deleteBatchByIds(ids) > 0
        if (flag) {
            list.forEach { sysOssConfig: SysOssConfig ->
                CacheUtils.evict(
                    CacheNames.SYS_OSS_CONFIG,
                    sysOssConfig.configKey
                )
            }
        }
        return flag
    }

    /**
     * 判断configKey是否唯一
     */
    private fun checkConfigKeyUnique(sysOssConfig: SysOssConfig): Boolean {
        val ossConfigId = if (ObjectUtil.isNull(sysOssConfig.ossConfigId)) -1L else sysOssConfig.ossConfigId
        val info = baseMapper.selectOneByQuery(
            QueryWrapper.create().select(SYS_OSS_CONFIG.OSS_CONFIG_ID, SYS_OSS_CONFIG.CONFIG_KEY)
                .from(SYS_OSS_CONFIG)
                .where(SYS_OSS_CONFIG.CONFIG_KEY.eq(sysOssConfig.configKey))
        )
        return !(ObjectUtil.isNotNull(info) && info.ossConfigId !== ossConfigId)
    }

    /**
     * 启用禁用状态
     */
    @Transactional(rollbackFor = [Exception::class])
    override fun updateOssConfigStatus(bo: SysOssConfigBo): Int {
        val sysOssConfig = MapstructUtils.convert(bo, SysOssConfig::class.java)!!
        val updateOldStatus = UpdateChain.of(SysOssConfig::class.java).set(SysOssConfig::status, "1")
            .where(SysOssConfig::status).eq("0")
            .update()
        val row = baseMapper.update(sysOssConfig)
        if (updateOldStatus || row > 0) {
            RedisUtils.setCacheObject(OssConstant.DEFAULT_CONFIG_KEY, sysOssConfig.configKey)
        }
        return row
    }

}
