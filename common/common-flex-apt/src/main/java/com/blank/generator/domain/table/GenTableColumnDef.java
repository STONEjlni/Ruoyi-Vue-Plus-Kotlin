package com.blank.generator.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class GenTableColumnDef extends TableDef {

    /**
     * 代码生成业务字段表 gen_table_column
     */
    public static final GenTableColumnDef GEN_TABLE_COLUMN = new GenTableColumnDef();

    /**
     * 是否主键（1是）
     */
    public final QueryColumn IS_PK = new QueryColumn(this, "is_pk");

    /**
     * 排序
     */
    public final QueryColumn SORT = new QueryColumn(this, "sort");

    /**
     * 是否编辑字段（1是）
     */
    public final QueryColumn IS_EDIT = new QueryColumn(this, "is_edit");

    /**
     * 是否列表字段（1是）
     */
    public final QueryColumn IS_LIST = new QueryColumn(this, "is_list");

    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * 是否查询字段（1是）
     */
    public final QueryColumn IS_QUERY = new QueryColumn(this, "is_query");

    /**
     * 归属表编号
     */
    public final QueryColumn TABLE_ID = new QueryColumn(this, "table_id");

    public final QueryColumn VERSION = new QueryColumn(this, "version");

    /**
     * 编号
     */
    public final QueryColumn COLUMN_ID = new QueryColumn(this, "column_id");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 字典类型
     */
    public final QueryColumn DICT_TYPE = new QueryColumn(this, "dict_type");

    /**
     * 显示类型（input文本框、textarea文本域、select下拉框、checkbox复选框、radio单选框、datetime日期控件、image图片上传控件、upload文件上传控件、editor富文本控件）
     */
    public final QueryColumn HTML_TYPE = new QueryColumn(this, "html_type");

    /**
     * 是否为插入字段（1是）
     */
    public final QueryColumn IS_INSERT = new QueryColumn(this, "is_insert");

    /**
     * JAVA类型
     */
    public final QueryColumn JAVA_TYPE = new QueryColumn(this, "java_type");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * JAVA字段名
     */
    public final QueryColumn JAVA_FIELD = new QueryColumn(this, "java_field");

    /**
     * 查询方式（EQ等于、NE不等于、GT大于、LT小于、LIKE模糊、BETWEEN范围）
     */
    public final QueryColumn QUERY_TYPE = new QueryColumn(this, "query_type");

    /**
     * 列名称
     */
    public final QueryColumn COLUMN_NAME = new QueryColumn(this, "column_name");

    /**
     * 列类型
     */
    public final QueryColumn COLUMN_TYPE = new QueryColumn(this, "column_type");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 是否必填（1是）
     */
    public final QueryColumn IS_REQUIRED = new QueryColumn(this, "is_required");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 是否自增（1是）
     */
    public final QueryColumn IS_INCREMENT = new QueryColumn(this, "is_increment");

    /**
     * 列描述
     */
    public final QueryColumn COLUMN_COMMENT = new QueryColumn(this, "column_comment");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{IS_PK, SORT, IS_EDIT, IS_LIST, IS_QUERY, TABLE_ID, VERSION, COLUMN_ID, CREATE_BY, DICT_TYPE, HTML_TYPE, IS_INSERT, JAVA_TYPE, UPDATE_BY, JAVA_FIELD, QUERY_TYPE, COLUMN_NAME, COLUMN_TYPE, CREATE_TIME, IS_REQUIRED, UPDATE_TIME, IS_INCREMENT, COLUMN_COMMENT};

    public GenTableColumnDef() {
        super("", "gen_table_column");
    }

}
