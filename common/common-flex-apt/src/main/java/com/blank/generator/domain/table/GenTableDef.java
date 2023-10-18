package com.blank.generator.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class GenTableDef extends TableDef {

    /**
     * 代码生成表 gen_table
     */
    public static final GenTableDef GEN_TABLE = new GenTableDef();

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * 生成路径（不填默认项目路径）
     */
    public final QueryColumn GEN_PATH = new QueryColumn(this, "gen_path");

    /**
     * 生成代码方式（0zip压缩包 1自定义路径）
     */
    public final QueryColumn GEN_TYPE = new QueryColumn(this, "gen_type");

    /**
     * 其它生成选项
     */
    public final QueryColumn OPTIONS = new QueryColumn(this, "options");

    /**
     * 编号
     */
    public final QueryColumn TABLE_ID = new QueryColumn(this, "table_id");

    public final QueryColumn VERSION = new QueryColumn(this, "version");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 数据源名称
     */
    public final QueryColumn DATA_NAME = new QueryColumn(this, "data_name");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 实体类名称(首字母大写)
     */
    public final QueryColumn CLASS_NAME = new QueryColumn(this, "class_name");

    /**
     * 表名称
     */
    public final QueryColumn TABLE_NAME = new QueryColumn(this, "table_name");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 生成模块名
     */
    public final QueryColumn MODULE_NAME = new QueryColumn(this, "module_name");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 生成包路径
     */
    public final QueryColumn PACKAGE_NAME = new QueryColumn(this, "package_name");

    /**
     * 使用的模板（crud单表操作 tree树表操作 sub主子表操作）
     */
    public final QueryColumn TPL_CATEGORY = new QueryColumn(this, "tpl_category");

    /**
     * 生成业务名
     */
    public final QueryColumn BUSINESS_NAME = new QueryColumn(this, "business_name");

    /**
     * 生成功能名
     */
    public final QueryColumn FUNCTION_NAME = new QueryColumn(this, "function_name");

    /**
     * 关联父表的表名
     */
    public final QueryColumn SUB_TABLE_NAME = new QueryColumn(this, "sub_table_name");

    /**
     * 表描述
     */
    public final QueryColumn TABLE_COMMENT = new QueryColumn(this, "table_comment");

    /**
     * 生成作者
     */
    public final QueryColumn FUNCTION_AUTHOR = new QueryColumn(this, "function_author");

    /**
     * 本表关联父表的外键名
     */
    public final QueryColumn SUB_TABLE_FK_NAME = new QueryColumn(this, "sub_table_fk_name");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{REMARK, GEN_PATH, GEN_TYPE, OPTIONS, TABLE_ID, VERSION, CREATE_BY, DATA_NAME, UPDATE_BY, CLASS_NAME, TABLE_NAME, CREATE_TIME, MODULE_NAME, UPDATE_TIME, PACKAGE_NAME, TPL_CATEGORY, BUSINESS_NAME, FUNCTION_NAME, SUB_TABLE_NAME, TABLE_COMMENT, FUNCTION_AUTHOR, SUB_TABLE_FK_NAME};

    public GenTableDef() {
        super("", "gen_table");
    }

}
