package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysDictTypeDef extends TableDef {

    /**
     * 字典类型表 sys_dict_type
     */
    public static final SysDictTypeDef SYS_DICT_TYPE = new SysDictTypeDef();

    /**
     * 字典主键
     */
    public final QueryColumn DICT_ID = new QueryColumn(this, "dict_id");

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 字典名称
     */
    public final QueryColumn DICT_NAME = new QueryColumn(this, "dict_name");

    /**
     * 字典类型
     */
    public final QueryColumn DICT_TYPE = new QueryColumn(this, "dict_type");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{DICT_ID, REMARK, CREATE_BY, DICT_NAME, DICT_TYPE, UPDATE_BY, CREATE_DEPT, CREATE_TIME, UPDATE_TIME};

    public SysDictTypeDef() {
        super("", "sys_dict_type");
    }

}
