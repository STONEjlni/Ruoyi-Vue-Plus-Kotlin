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
import com.blank.system.domain.bo.SysDictTypeBo
import com.blank.system.domain.vo.SysDictTypeVo
import com.blank.system.service.ISysDictTypeService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 数据字典信息
 */
@Validated
@RestController
@RequestMapping("/system/dict/type")
class SysDictTypeController(
    private val dictTypeService: ISysDictTypeService
) : BaseController() {

    /**
     * 查询字典类型列表
     */
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    fun list(dictType: SysDictTypeBo, pageQuery: PageQuery): TableDataInfo<SysDictTypeVo>? {
        return dictTypeService.selectPageDictTypeList(dictType, pageQuery)
    }

    /**
     * 查询字典类型详细
     *
     * @param dictId 字典ID
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = ["/{dictId}"])
    fun getInfo(@PathVariable dictId: Long): R<SysDictTypeVo> {
        return ok(data = dictTypeService.selectDictTypeById(dictId))
    }

    /**
     * 新增字典类型
     */
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典类型", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody dict: SysDictTypeBo): R<Unit> {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return fail(msg = "新增字典'${dict.dictName}'失败，字典类型已存在")
        }
        dictTypeService.insertDictType(dict)
        return ok()
    }

    /**
     * 修改字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典类型", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody dict: SysDictTypeBo): R<Unit> {
        if (!dictTypeService.checkDictTypeUnique(dict)) {
            return fail(msg = "修改字典'${dict.dictName}'失败，字典类型已存在")
        }
        dictTypeService.updateDictType(dict)
        return ok()
    }

    /**
     * 删除字典类型
     *
     * @param dictIds 字典ID串
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictIds}")
    fun remove(@PathVariable dictIds: Array<Long>): R<Unit> {
        dictTypeService.deleteDictTypeByIds(dictIds)
        return ok()
    }

    /**
     * 刷新字典缓存
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.CLEAN)
    @DeleteMapping("/refreshCache")
    fun refreshCache(): R<Unit> {
        dictTypeService.resetDictCache()
        return ok()
    }

    /**
     * 获取字典选择框列表
     */
    @GetMapping("/optionselect")
    fun optionselect(): R<MutableList<SysDictTypeVo>> {
        val dictTypes = dictTypeService.selectDictTypeAll()
        return ok(data = dictTypes)
    }
}
