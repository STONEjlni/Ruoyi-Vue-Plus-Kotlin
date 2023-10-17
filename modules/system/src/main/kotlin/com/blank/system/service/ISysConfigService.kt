package com.blank.system.service

import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.system.domain.bo.SysConfigBo
import com.blank.system.domain.vo.SysConfigVo

/**
 * 参数配置 服务层
 */
interface ISysConfigService {
    fun selectPageConfigList(config: SysConfigBo, pageQuery: PageQuery): TableDataInfo<SysConfigVo>?

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    fun selectConfigById(configId: Long): SysConfigVo?

    /**
     * 根据键名查询参数配置信息
     *
     * @param configKey 参数键名
     * @return 参数键值
     */
    fun selectConfigByKey(configKey: String): String?

    /**
     * 获取注册开关
     *
     * @param tenantId 租户id
     * @return true开启，false关闭
     */
    fun selectRegisterEnabled(tenantId: String): Boolean

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    fun selectConfigList(config: SysConfigBo): MutableList<SysConfigVo>?

    /**
     * 新增参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    fun insertConfig(bo: SysConfigBo): String?

    /**
     * 修改参数配置
     *
     * @param bo 参数配置信息
     * @return 结果
     */
    fun updateConfig(bo: SysConfigBo): String?

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    fun deleteConfigByIds(configIds: Array<Long>)

    /**
     * 重置参数缓存数据
     */
    fun resetConfigCache()

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数信息
     * @return 结果
     */
    fun checkConfigKeyUnique(config: SysConfigBo): Boolean
}
