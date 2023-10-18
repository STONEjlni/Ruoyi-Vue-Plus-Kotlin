package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysDictDataDef extends TableDef {

    /**
     * 字典数据表 sys_dict_data
     */
    public static final SysDictDataDef SYS_DICT_DATA = new SysDictDataDef();

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    public final QueryColumn VERSION = new QueryColumn(this, "version");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 样式属性（其他样式扩展）
     */
    public final QueryColumn CSS_CLASS = new QueryColumn(this, "css_class");

    /**
     * 字典编码
     */
    public final QueryColumn DICT_CODE = new QueryColumn(this, "dict_code");

    /**
     * 字典排序
     */
    public final QueryColumn DICT_SORT = new QueryColumn(this, "dict_sort");

    /**
     * 字典类型
     */
    public final QueryColumn DICT_TYPE = new QueryColumn(this, "dict_type");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 字典标签
     */
    public final QueryColumn DICT_LABEL = new QueryColumn(this, "dict_label");

    /**
     * 字典键值
     */
    public final QueryColumn DICT_VALUE = new QueryColumn(this, "dict_value");

    /**
     * 是否默认（Y是 N否）
     */
    public final QueryColumn IS_DEFAULT = new QueryColumn(this, "is_default");

    /**
     * 表格字典样式
     */
    public final QueryColumn LIST_CLASS = new QueryColumn(this, "list_class");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{REMARK, VERSION, CREATE_BY, CSS_CLASS, DICT_CODE, DICT_SORT, DICT_TYPE, UPDATE_BY, DICT_LABEL, DICT_VALUE, IS_DEFAULT, LIST_CLASS, CREATE_TIME, UPDATE_TIME};

    public SysDictDataDef() {
        super("", "sys_dict_data");
    }

}
