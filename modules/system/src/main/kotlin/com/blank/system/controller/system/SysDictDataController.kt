package com.blank.system.controller.system

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.web.core.BaseController
import com.blank.system.domain.bo.SysDictDataBo
import com.blank.system.domain.vo.SysDictDataVo
import com.blank.system.service.ISysDictDataService
import com.blank.system.service.ISysDictTypeService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

/**
 * 数据字典信息
 */
@Validated
@RestController
@RequestMapping("/system/dict/data")
class SysDictDataController(
    private val dictDataService: ISysDictDataService,
    private val dictTypeService: ISysDictTypeService
) : BaseController() {

    /**
     * 查询字典数据列表
     */
    @SaCheckPermission("system:dict:list")
    @GetMapping("/list")
    fun list(dictData: SysDictDataBo, pageQuery: PageQuery): TableDataInfo<SysDictDataVo>? {
        return dictDataService.selectPageDictDataList(dictData, pageQuery)
    }

    /**
     * 查询字典数据详细
     *
     * @param dictCode 字典code
     */
    @SaCheckPermission("system:dict:query")
    @GetMapping(value = ["/{dictCode}"])
    fun getInfo(@PathVariable dictCode: Long): R<SysDictDataVo> {
        return ok(data = dictDataService.selectDictDataById(dictCode))
    }

    /**
     * 根据字典类型查询字典数据信息
     *
     * @param dictType 字典类型
     */
    @GetMapping(value = ["/type/{dictType}"])
    fun dictType(@PathVariable dictType: String): R<MutableList<SysDictDataVo>> {
        var data = dictTypeService.selectDictDataByType(dictType)
        if (ObjectUtil.isNull(data)) {
            data = ArrayList()
        }
        return ok(data = data)
    }

    /**
     * 新增字典类型
     */
    @SaCheckPermission("system:dict:add")
    @Log(title = "字典数据", businessType = BusinessType.INSERT)
    @PostMapping
    fun add(@Validated @RequestBody dict: SysDictDataBo): R<Unit> {
        dictDataService.insertDictData(dict)
        return ok()
    }

    /**
     * 修改保存字典类型
     */
    @SaCheckPermission("system:dict:edit")
    @Log(title = "字典数据", businessType = BusinessType.UPDATE)
    @PutMapping
    fun edit(@Validated @RequestBody dict: SysDictDataBo): R<Unit> {
        dictDataService.updateDictData(dict)
        return ok()
    }

    /**
     * 删除字典类型
     *
     * @param dictCodes 字典code串
     */
    @SaCheckPermission("system:dict:remove")
    @Log(title = "字典类型", businessType = BusinessType.DELETE)
    @DeleteMapping("/{dictCodes}")
    fun remove(@PathVariable dictCodes: Array<Long>): R<Unit> {
        dictDataService.deleteDictDataByIds(dictCodes)
        return ok()
    }
}
