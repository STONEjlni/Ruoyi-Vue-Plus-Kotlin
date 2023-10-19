package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysConfigBo
import com.blank.system.domain.vo.SysConfigVo
import com.blank.system.service.ISysConfigService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 参数配置 信息操作处理
 */
@Validated
@RestController
@RequestMapping("/system/config")
class SysConfigController(
    private val configService: ISysConfigService
) : BaseController() {


    /**
     * 获取参数配置列表
     */
    @SaCheckPermission("system:config:list")
    @GetMapping("/list")
    fun list(config: SysConfigBo, pageQuery: PageQuery): TableDataInfo<SysConfigVo>? {
        return configService.selectPageConfigList(config, pageQuery)
    }

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 参数ID
     */
    @SaCheckPermission("system:config:query")
    @GetMapping(value = ["/{configId}"])
    fun getInfo(@PathVariable configId: Long): R<SysConfigVo> {
        return ok(data = configService.selectConfigById(configId))
    }

    /**
     * 根据参数键名查询参数值
     *
     * @param configKey 参数Key
     */
    @GetMapping(value = ["/configKey/{configKey}"])
    fun getConfigKey(@PathVariable configKey: String): R<String> {
        return ok(msg = "操作成功", data = configService.selectConfigByKey(configKey))
    }

    /**
     * 新增参数配置
     */
    @SaCheckPermission("system:config:add")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody config: SysConfigBo): R<Unit> {
        if (!configService.checkConfigKeyUnique(config)) {
            return fail(msg = "新增参数'${config.configName}'失败，参数键名已存在")
        }
        configService.insertConfig(config)
        return ok()
    }

    /**
     * 修改参数配置
     */
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody config: SysConfigBo): R<Unit> {
        if (!configService.checkConfigKeyUnique(config)) {
            return fail(msg = "修改参数'${config.configName}'失败，参数键名已存在")
        }
        configService.updateConfig(config)
        return ok()
    }

    /**
     * 根据参数键名修改参数配置
     */
    @SaCheckPermission("system:config:edit")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    fun updateByKey(@RequestBody config: SysConfigBo): R<Unit> {
        configService.updateConfig(config)
        return ok()
    }

    /**
     * 删除参数配置
     *
     * @param configIds 参数ID串
     */
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    fun remove(@PathVariable configIds: Array<Long>): R<Unit> {
        configService.deleteConfigByIds(configIds)
        return ok()
    }

    /**
     * 刷新参数缓存
     */
    @SaCheckPermission("system:config:remove")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    fun refreshCache(): R<Unit> {
        configService.resetConfigCache()
        return ok()
    }
}
