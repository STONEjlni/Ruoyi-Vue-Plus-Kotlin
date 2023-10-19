package com.blank.generator.util

import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.StringUtilsExtend.convertToCamelCase
import com.blank.common.core.utils.StringUtilsExtend.toCamelCase
import com.blank.generator.config.GenConfig
import com.blank.generator.constant.GenConstants
import com.blank.generator.domain.GenTable
import com.blank.generator.domain.GenTableColumn
import org.apache.commons.lang3.RegExUtils
import org.apache.commons.lang3.StringUtils
import java.util.*

/**
 * 代码生成器 工具类
 */
object GenUtils {

    /**
     * 初始化表信息
     */
    fun initTable(genTable: GenTable, operId: Long?) {
        genTable.className = convertClassName(genTable.tableName!!)
        genTable.packageName = GenConfig.packageName
        genTable.moduleName = getModuleName(GenConfig.packageName!!)
        genTable.businessName = getBusinessName(genTable.tableName!!)
        genTable.functionName = replaceText(genTable.tableComment)
        genTable.functionAuthor = GenConfig.author
        genTable.createBy = operId
    }

    /**
     * 初始化列属性字段
     */
    fun initColumnField(column: GenTableColumn, table: GenTable) {
        val dataType = getDbType(column.columnType)
        val columnName = column.columnName
        column.tableId = table.tableId
        column.createBy = table.createBy
        // 设置java字段名
        column.javaField = toCamelCase(columnName!!)
        // 设置默认类型
        column.javaType = GenConstants.TYPE_STRING
        column.queryType = GenConstants.QUERY_EQ
        if (arraysContains(GenConstants.COLUMNTYPE_STR, dataType) || arraysContains(
                GenConstants.COLUMNTYPE_TEXT,
                dataType
            )
        ) {
            // 字符串长度超过500设置为文本域
            val columnLength = getColumnLength(column.columnType)
            val htmlType = if (columnLength >= 500 || arraysContains(
                    GenConstants.COLUMNTYPE_TEXT,
                    dataType
                )
            ) GenConstants.HTML_TEXTAREA else GenConstants.HTML_INPUT
            column.htmlType = htmlType
        } else if (arraysContains(GenConstants.COLUMNTYPE_TIME, dataType)) {
            column.javaType = GenConstants.TYPE_DATE
            column.htmlType = GenConstants.HTML_DATETIME
        } else if (arraysContains(GenConstants.COLUMNTYPE_NUMBER, dataType)) {
            column.htmlType = GenConstants.HTML_INPUT

            // 如果是浮点型 统一用BigDecimal
            val str = StringUtils.split(
                StringUtils.substringBetween(column.columnType, "(", ")"),
                StringUtilsExtend.SEPARATOR
            )
            if (str != null && str.size == 2 && str[1].toInt() > 0) {
                column.javaType = GenConstants.TYPE_BIGDECIMAL
            } else if (str != null && str.size == 1 && str[0].toInt() <= 10) {
                column.javaType = GenConstants.TYPE_INTEGER
            } else {
                column.javaType = GenConstants.TYPE_LONG
            }
        }

        // BO对象 默认插入勾选
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_ADD, columnName) && !column.isPk()) {
            column.isInsert = GenConstants.REQUIRE
        }
        // BO对象 默认编辑勾选
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName)) {
            column.isEdit = GenConstants.REQUIRE
        }
        // BO对象 默认是否必填勾选
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_EDIT, columnName)) {
            column.isRequired = GenConstants.REQUIRE
        }
        // VO对象 默认返回勾选
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_LIST, columnName)) {
            column.isList = GenConstants.REQUIRE
        }
        // BO对象 默认查询勾选
        if (!arraysContains(GenConstants.COLUMNNAME_NOT_QUERY, columnName) && !column.isPk()) {
            column.isQuery = GenConstants.REQUIRE
        }

        // 查询字段类型
        if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
            column.queryType = GenConstants.QUERY_LIKE
        }
        // 状态字段设置单选框
        if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
            column.htmlType = GenConstants.HTML_RADIO
        } else if (StringUtils.endsWithIgnoreCase(columnName, "type")
            || StringUtils.endsWithIgnoreCase(columnName, "sex")
        ) {
            column.htmlType = GenConstants.HTML_SELECT
        } else if (StringUtils.endsWithIgnoreCase(columnName, "image")) {
            column.htmlType = GenConstants.HTML_IMAGE_UPLOAD
        } else if (StringUtils.endsWithIgnoreCase(columnName, "file")) {
            column.htmlType = GenConstants.HTML_FILE_UPLOAD
        } else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
            column.htmlType = GenConstants.HTML_EDITOR
        }
    }

    /**
     * 校验数组是否包含指定值
     *
     * @param arr         数组
     * @param targetValue 值
     * @return 是否包含
     */
    fun arraysContains(arr: Array<String>, targetValue: String?): Boolean {
        return Arrays.asList(*arr).contains(targetValue)
    }

    /**
     * 获取模块名
     *
     * @param packageName 包名
     * @return 模块名
     */
    fun getModuleName(packageName: String): String {
        val lastIndex = packageName.lastIndexOf(".")
        val nameLength = packageName.length
        return StringUtils.substring(packageName, lastIndex + 1, nameLength)
    }

    /**
     * 获取业务名
     *
     * @param tableName 表名
     * @return 业务名
     */
    fun getBusinessName(tableName: String): String {
        val firstIndex = tableName.indexOf("_")
        val nameLength = tableName.length
        var businessName = StringUtils.substring(tableName, firstIndex + 1, nameLength)
        businessName = toCamelCase(businessName!!)
        return businessName
    }

    /**
     * 表名转换成Java类名
     *
     * @param tableName 表名称
     * @return 类名
     */
    fun convertClassName(tableName: String): String {
        var tableName = tableName
        val autoRemovePre: Boolean = GenConfig.autoRemovePre
        val tablePrefix: String = GenConfig.tablePrefix!!
        if (autoRemovePre && StringUtils.isNotEmpty(tablePrefix)) {
            val searchList = StringUtils.split(tablePrefix, StringUtilsExtend.SEPARATOR)
            tableName = replaceFirst(tableName, searchList)
        }
        return convertToCamelCase(tableName)
    }

    /**
     * 批量替换前缀
     *
     * @param replacementm 替换值
     * @param searchList   替换列表
     */
    fun replaceFirst(replacementm: String, searchList: Array<String>): String {
        var text = replacementm
        for (searchString in searchList) {
            if (replacementm.startsWith(searchString)) {
                text = replacementm.replaceFirst(searchString.toRegex(), StringUtils.EMPTY)
                break
            }
        }
        return text
    }

    /**
     * 关键字替换
     *
     * @param text 需要被替换的名字
     * @return 替换后的名字
     */
    fun replaceText(text: String?): String {
        return RegExUtils.replaceAll(text, "(?:表|若依)", "")
    }

    /**
     * 获取数据库类型字段
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    fun getDbType(columnType: String?): String? {
        return if (StringUtils.indexOf(columnType, "(") > 0) {
            StringUtils.substringBefore(columnType, "(")
        } else {
            columnType
        }
    }

    /**
     * 获取字段长度
     *
     * @param columnType 列类型
     * @return 截取后的列类型
     */
    fun getColumnLength(columnType: String?): Int {
        return if (StringUtils.indexOf(columnType, "(") > 0) {
            val length = StringUtils.substringBetween(columnType, "(", ")")
            length.toInt()
        } else {
            0
        }
    }
}
