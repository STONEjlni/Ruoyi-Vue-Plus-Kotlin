package com.blank.generator.constant

/**
 * 代码生成通用常量
 */
interface GenConstants {
    companion object {
        /**
         * 单表（增删改查）
         */
        const val TPL_CRUD = "crud"

        /**
         * 树表（增删改查）
         */
        const val TPL_TREE = "tree"

        /**
         * 树编码字段
         */
        const val TREE_CODE = "treeCode"

        /**
         * 树父编码字段
         */
        const val TREE_PARENT_CODE = "treeParentCode"

        /**
         * 树名称字段
         */
        const val TREE_NAME = "treeName"

        /**
         * 上级菜单ID字段
         */
        const val PARENT_MENU_ID = "parentMenuId"

        /**
         * 上级菜单名称字段
         */
        const val PARENT_MENU_NAME = "parentMenuName"

        /**
         * 数据库字符串类型
         */
        val COLUMNTYPE_STR = arrayOf("char", "varchar", "enum", "set", "nchar", "nvarchar", "varchar2", "nvarchar2")

        /**
         * 数据库文本类型
         */
        val COLUMNTYPE_TEXT = arrayOf(
            "tinytext", "text", "mediumtext", "longtext", "binary", "varbinary", "blob",
            "ntext", "image", "bytea"
        )

        /**
         * 数据库时间类型
         */
        val COLUMNTYPE_TIME = arrayOf(
            "datetime", "time", "date", "timestamp", "year", "interval",
            "smalldatetime", "datetime2", "datetimeoffset"
        )

        /**
         * 数据库数字类型
         */
        val COLUMNTYPE_NUMBER = arrayOf(
            "tinyint", "smallint", "mediumint", "int", "number", "integer",
            "bit", "bigint", "float", "double", "decimal", "numeric", "real", "double precision",
            "smallserial", "serial", "bigserial", "money", "smallmoney"
        )

        /**
         * BO对象 不需要添加字段
         */
        val COLUMNNAME_NOT_ADD = arrayOf(
            "create_dept", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "version", "tenant_id"
        )

        /**
         * BO对象 不需要编辑字段
         */
        val COLUMNNAME_NOT_EDIT = arrayOf(
            "create_dept", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "version", "tenant_id"
        )

        /**
         * VO对象 不需要返回字段
         */
        val COLUMNNAME_NOT_LIST = arrayOf(
            "create_dept", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "version", "tenant_id"
        )

        /**
         * BO对象 不需要查询字段
         */
        val COLUMNNAME_NOT_QUERY = arrayOf(
            "id", "create_dept", "create_by", "create_time", "del_flag", "update_by",
            "update_time", "remark", "version", "tenant_id"
        )

        /**
         * Entity基类字段
         */
        var BASE_ENTITY = arrayOf("createDept", "createBy", "createTime", "updateBy", "updateTime", "tenantId")

        /**
         * 文本框
         */
        const val HTML_INPUT = "input"

        /**
         * 文本域
         */
        const val HTML_TEXTAREA = "textarea"

        /**
         * 下拉框
         */
        const val HTML_SELECT = "select"

        /**
         * 单选框
         */
        const val HTML_RADIO = "radio"

        /**
         * 复选框
         */
        const val HTML_CHECKBOX = "checkbox"

        /**
         * 日期控件
         */
        const val HTML_DATETIME = "datetime"

        /**
         * 图片上传控件
         */
        const val HTML_IMAGE_UPLOAD = "imageUpload"

        /**
         * 文件上传控件
         */
        const val HTML_FILE_UPLOAD = "fileUpload"

        /**
         * 富文本控件
         */
        const val HTML_EDITOR = "editor"

        /**
         * 字符串类型
         */
        const val TYPE_STRING = "String"

        /**
         * 整型
         */
        const val TYPE_INTEGER = "Integer"

        /**
         * 长整型
         */
        const val TYPE_LONG = "Long"

        /**
         * 浮点型
         */
        const val TYPE_DOUBLE = "Double"

        /**
         * 高精度计算类型
         */
        const val TYPE_BIGDECIMAL = "BigDecimal"

        /**
         * 时间类型
         */
        const val TYPE_DATE = "Date"

        /**
         * 模糊查询
         */
        const val QUERY_LIKE = "LIKE"

        /**
         * 相等查询
         */
        const val QUERY_EQ = "EQ"

        /**
         * 需要
         */
        const val REQUIRE = "1"
    }
}
