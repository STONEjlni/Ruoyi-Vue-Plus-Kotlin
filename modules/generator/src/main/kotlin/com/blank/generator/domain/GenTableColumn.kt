package com.blank.generator.domain

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

    /**
     * 是否自增（1是）
     */
    var isIncrement: String? = null

    /**
     * 是否必填（1是）
     */
    var isRequired: String? = null

    /**
     * 是否为插入字段（1是）
     */
    var isInsert: String? = null

    /**
     * 是否编辑字段（1是）
     */
    var isEdit: String? = null

    /**
     * 是否列表字段（1是）
     */
    var isList: String? = null

    /**
     * 是否查询字段（1是）
     */
    var isQuery: String? = null

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

    fun getCapJavaField(): String {
        return StringUtils.capitalize(javaField)
    }

    @JvmOverloads
    fun isPk(isPk: String? = this.isPk): Boolean {
        return isPk != null && StringUtils.equals("1", isPk)
    }

    @JvmOverloads
    fun isIncrement(isIncrement: String? = this.isIncrement): Boolean {
        return isIncrement != null && StringUtils.equals("1", isIncrement)
    }

    @JvmOverloads
    fun isRequired(isRequired: String? = this.isRequired): Boolean {
        return isRequired != null && StringUtils.equals("1", isRequired)
    }

    @JvmOverloads
    fun isInsert(isInsert: String? = this.isInsert): Boolean {
        return isInsert != null && StringUtils.equals("1", isInsert)
    }

    @JvmOverloads
    fun isEdit(isEdit: String? = this.isEdit): Boolean {
        return isEdit != null && StringUtils.equals("1", isEdit)
    }

    @JvmOverloads
    fun isList(isList: String? = this.isList): Boolean {
        return isList != null && StringUtils.equals("1", isList)
    }

    @JvmOverloads
    fun isQuery(isQuery: String? = this.isQuery): Boolean {
        return isQuery != null && StringUtils.equals("1", isQuery)
    }

    fun isSuperColumn(): Boolean {
        return isSuperColumn(this.javaField)
    }

    fun isUsableColumn(): Boolean {
        return isUsableColumn(this.javaField)
    }


    fun readConverterExp(): String? {
        var remarks = StringUtils.substringBetween(columnComment, "（", "）")
        var sb = StringBuffer()
        return if (StringUtils.isNotEmpty(remarks)) {
            for (value in remarks.split(" ").toTypedArray()) {
                if (StringUtils.isNotEmpty(value)) {
                    var startStr: Any = value.subSequence(0, 1)
                    var endStr = value.substring(1)
                    sb.append(StringUtils.EMPTY).append(startStr).append("=").append(endStr)
                        .append(StringUtilsExtend.SEPARATOR)
                }
            }
            sb.deleteCharAt(sb.length - 1).toString()
        } else {
            columnComment
        }
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

