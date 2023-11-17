package com.blank.generator.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.mybatis.core.domain.BaseEntity
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.Table
import jakarta.validation.constraints.NotBlank
import org.apache.commons.lang3.StringUtils

/**
 * 代码生成业务字段表 gen_table_column
 */
@Table("gen_table_column")
@Open
@NoArg
class GenTableColumn : BaseEntity() {
    /**
     * 编号
     */
    @Id
    var columnId: Long? = null

    /**
     * 归属表编号
     */
    var tableId: Long? = null

    /**
     * 列名称
     */
    var columnName: String? = null

    /**
     * 列描述
     */
    var columnComment: String? = null

    /**
     * 列类型
     */
    var columnType: String? = null

    /**
     * JAVA类型
     */
    var javaType: String? = null

    /**
     * JAVA字段名
     */
    var javaField: @NotBlank(message = "Java属性不能为空") String? = null

    /**
     * 是否主键（1是）
     */
    var isPk: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 是否自增（1是）
     */
    var isIncrement: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 是否必填（1是）
     */
    var isRequired: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 是否为插入字段（1是）
     */
    var isInsert: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 是否编辑字段（1是）
     */
    var isEdit: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 是否列表字段（1是）
     */
    var isList: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 是否查询字段（1是）
     */
    var isQuery: String? = null
        set(value) {
            field = StringUtilsExtend.boolStrTo01(value.let { "false" })
        }

    /**
     * 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围）
     */
    var queryType: String? = null

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    var htmlType: String? = null

    /**
     * 字典类型
     */
    var dictType: String? = null

    /**
     * 排序
     */
    var sort: Int? = null
    val capJavaField: String
        get() = StringUtils.capitalize(javaField)

    fun isPk(): Boolean {
        return isPk(isPk)
    }

    fun isPk(isPk: String?): Boolean {
        return isPk != null && StringUtils.equals("1", isPk)
    }

    fun isIncrement(): Boolean {
        return isIncrement(isIncrement)
    }

    fun isIncrement(isIncrement: String?): Boolean {
        return isIncrement != null && StringUtils.equals("1", isIncrement)
    }

    fun isRequired(): Boolean {
        return isRequired(isRequired)
    }

    fun isRequired(isRequired: String?): Boolean {
        return isRequired != null && StringUtils.equals("1", isRequired)
    }

    fun isInsert(): Boolean {
        return isInsert(isInsert)
    }

    fun isInsert(isInsert: String?): Boolean {
        return isInsert != null && StringUtils.equals("1", isInsert)
    }

    fun isEdit(): Boolean {
        return isInsert(isEdit)
    }

    fun isEdit(isEdit: String?): Boolean {
        return isEdit != null && StringUtils.equals("1", isEdit)
    }

    fun isList(): Boolean {
        return isList(isList)
    }

    fun isList(isList: String?): Boolean {
        return StringUtils.equals("1", isList)
    }

    fun isQuery(): Boolean {
        return isQuery(isQuery)
    }

    fun isQuery(isQuery: String?): Boolean {
        return isQuery != null && StringUtils.equals("1", isQuery)
    }

    val isSuperColumn: Boolean
        get() = isSuperColumn(javaField)
    val isUsableColumn: Boolean
        get() = isUsableColumn(javaField)

    fun readConverterExp(): String? {
        val remarks = StringUtils.substringBetween(columnComment, "（", "）")
        val sb = StringBuffer()
        return if (StringUtils.isNotEmpty(remarks)) {
            for (value in remarks.split(" ").toTypedArray()) {
                if (StringUtils.isNotEmpty(value)) {
                    val startStr: Any = value.subSequence(0, 1)
                    val endStr = value.substring(1)
                    sb.append(StringUtils.EMPTY).append(startStr).append("=").append(endStr)
                        .append(StringUtilsExtend.SEPARATOR)
                }
            }
            sb.deleteCharAt(sb.length - 1).toString()
        } else {
            columnComment
        }
    }

    fun getIsPk(): String? {
        return isPk
    }

    fun setIsPk(isPk: String?) {
        this.isPk = isPk
    }

    fun getIsIncrement(): String? {
        return isIncrement
    }

    fun setIsIncrement(isIncrement: String?) {
        this.isIncrement = isIncrement
    }

    fun getIsRequired(): String? {
        return isRequired
    }

    fun setIsRequired(isRequired: String?) {
        this.isRequired = isRequired
    }

    fun getIsInsert(): String? {
        return isInsert
    }

    fun setIsInsert(isInsert: String?) {
        this.isInsert = isInsert
    }

    fun getIsEdit(): String? {
        return isEdit
    }

    fun setIsEdit(isEdit: String?) {
        this.isEdit = isEdit
    }

    fun getIsList(): String? {
        return isList
    }

    fun setIsList(isList: String?) {
        this.isList = isList
    }

    fun getIsQuery(): String? {
        return isQuery
    }

    fun setIsQuery(isQuery: String?) {
        this.isQuery = isQuery
    }

    companion object {
        fun isSuperColumn(javaField: String?): Boolean {
            return StringUtils.equalsAnyIgnoreCase(
                javaField,  // BaseEntity
                "createBy", "createTime", "updateBy", "updateTime",  // TreeEntity
                "parentName", "parentId"
            )
        }

        fun isUsableColumn(javaField: String?): Boolean {
            // isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
            return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum", "remark")
        }
    }
}

