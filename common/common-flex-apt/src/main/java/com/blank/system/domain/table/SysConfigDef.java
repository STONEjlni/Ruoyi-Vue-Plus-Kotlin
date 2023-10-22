package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysConfigDef extends TableDef {

    /**
     * 参数配置表 sys_config
     */
    public static final SysConfigDef SYS_CONFIG = new SysConfigDef();

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 参数主键
     */
    public final QueryColumn CONFIG_ID = new QueryColumn(this, "config_id");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 参数键名
     */
    public final QueryColumn CONFIG_KEY = new QueryColumn(this, "config_key");

    /**
     * 参数名称
     */
    public final QueryColumn CONFIG_NAME = new QueryColumn(this, "config_name");

    /**
     * 系统内置（Y是 N否）
     */
    public final QueryColumn CONFIG_TYPE = new QueryColumn(this, "config_type");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 参数键值
     */
    public final QueryColumn CONFIG_VALUE = new QueryColumn(this, "config_value");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{REMARK, CONFIG_ID, CREATE_BY, UPDATE_BY, CONFIG_KEY, CONFIG_NAME, CONFIG_TYPE, CREATE_DEPT, CREATE_TIME, UPDATE_TIME, CONFIG_VALUE};

    public SysConfigDef() {
        super("", "sys_config");
    }

}
