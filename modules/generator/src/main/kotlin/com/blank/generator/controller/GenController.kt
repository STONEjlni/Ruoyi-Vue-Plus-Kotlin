package com.blank.generator.controller

import cn.dev33.satoken.annotation.SaCheckPermission
import cn.hutool.core.convert.Convert
import cn.hutool.core.io.IoUtil
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessType
import com.blank.common.mybatis.core.page.PageQuery
import com.blank.common.mybatis.core.page.TableDataInfo
import com.blank.common.mybatis.helper.DataBaseHelper.getDataSourceNameList
import com.blank.common.web.core.BaseController
import com.blank.generator.domain.GenTable
import com.blank.generator.domain.GenTableColumn
import com.blank.generator.service.IGenTableService
import jakarta.servlet.http.HttpServletResponse
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.io.IOException

/**
 * 代码生成 操作处理
 */
@Validated
@RestController
@RequestMapping("/tool/gen")
class GenController(
    private val genTableService: IGenTableService
) : BaseController() {

    /**
     * 查询代码生成列表
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping("/list")
    fun genList(genTable: GenTable, pageQuery: PageQuery): TableDataInfo<GenTable>? {
        return genTableService.selectPageGenTableList(genTable, pageQuery)
    }

    /**
     * 修改代码生成业务
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:query")
    @GetMapping(value = ["/{tableId}"])
    fun getInfo(@PathVariable tableId: Long): R<Map<String, Any>> {
        val table = genTableService.selectGenTableById(tableId)!!
        val tables = genTableService.selectGenTableAll()!!
        val list = genTableService.selectGenTableColumnListByTableId(tableId)!!
        val map: MutableMap<String, Any> = mutableMapOf()
        map["info"] = table
        map["rows"] = list
        map["tables"] = tables
        return ok(data = map)
    }

    /**
     * 查询数据库列表
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping("/db/list")
    fun dataList(genTable: GenTable, pageQuery: PageQuery): TableDataInfo<GenTable>? {
        return genTableService.selectPageDbTableList(genTable, pageQuery)
    }

    /**
     * 查询数据表字段列表
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:list")
    @GetMapping(value = ["/column/{tableId}"])
    fun columnList(tableId: Long): TableDataInfo<GenTableColumn>? {
        val dataInfo = TableDataInfo<GenTableColumn>()
        val list = genTableService.selectGenTableColumnListByTableId(tableId)!!
        dataInfo.rows = list
        dataInfo.total = list.size.toLong()
        return dataInfo
    }

    /**
     * 导入表结构（保存）
     *
     * @param tables 表名串
     */
    @SaCheckPermission("tool:gen:import")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    fun importTableSave(tables: String, dataName: String): R<Void> {
        val tableNames = Convert.toStrArray(tables)
        // 查询表信息
        val tableList = genTableService.selectDbTableListByNames(tableNames, dataName)!!
        genTableService.importGenTable(tableList, dataName)
        return ok()
    }

    /**
     * 修改保存代码生成业务
     */
    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    fun editSave(@Validated @RequestBody genTable: GenTable): R<Void> {
        genTableService.validateEdit(genTable)
        genTableService.updateGenTable(genTable)
        return ok()
    }

    /**
     * 删除代码生成
     *
     * @param tableIds 表ID串
     */
    @SaCheckPermission("tool:gen:remove")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    fun remove(@PathVariable tableIds: Array<Long>): R<Void> {
        genTableService.deleteGenTableByIds(tableIds)
        return ok()
    }

    /**
     * 预览代码
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:preview")
    @GetMapping("/preview/{tableId}")
    @Throws(
        IOException::class
    )
    fun preview(@PathVariable("tableId") tableId: Long): R<Map<String, String>> {
        val dataMap = genTableService.previewCode(tableId)
        return ok(data = dataMap)
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableId}")
    @Throws(
        IOException::class
    )
    fun download(response: HttpServletResponse, @PathVariable("tableId") tableId: Long) {
        val data = genTableService.downloadCode(tableId)!!
        genCode(response, data)
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableId}")
    fun genCode(@PathVariable("tableId") tableId: Long): R<Void> {
        genTableService.generatorCode(tableId)
        return ok()
    }

    /**
     * 同步数据库
     *
     * @param tableId 表ID
     */
    @SaCheckPermission("tool:gen:edit")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableId}")
    fun synchDb(@PathVariable("tableId") tableId: Long): R<Void> {
        genTableService.synchDb(tableId)
        return ok()
    }

    /**
     * 批量生成代码
     *
     * @param tableIdStr 表ID串
     */
    @SaCheckPermission("tool:gen:code")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    @Throws(
        IOException::class
    )
    fun batchGenCode(response: HttpServletResponse, tableIdStr: String) {
        val tableIds = Convert.toStrArray(tableIdStr)
        val data = genTableService.downloadCode(tableIds)!!
        genCode(response, data)
    }

    /**
     * 生成zip文件
     */
    @Throws(IOException::class)
    private fun genCode(response: HttpServletResponse, data: ByteArray) {
        response.reset()
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition")
        response.setHeader("Content-Disposition", "attachment; filename=\"blank.zip\"")
        response.addHeader("Content-Length", "" + data.size)
        response.contentType = "application/octet-stream; charset=UTF-8"
        IoUtil.write(response.outputStream, false, data)
    }

    /**
     * 查询数据源名称列表
     */
    @GetMapping(value = ["/getDataNames"])
    @SaCheckPermission("tool:gen:list")
    fun getCurrentDataSourceNameList() = ok(data = getDataSourceNameList())
}

