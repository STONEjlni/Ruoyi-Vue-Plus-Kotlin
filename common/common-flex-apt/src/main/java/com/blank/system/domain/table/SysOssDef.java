package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysOssDef extends TableDef {

    /**
     * OSS对象存储对象
     */
    public static final SysOssDef SYS_OSS = new SysOssDef();

    /**
     * URL地址
     */
    public final QueryColumn URL = new QueryColumn(this, "url");

    /**
     * 对象存储主键
     */
    public final QueryColumn OSS_ID = new QueryColumn(this, "oss_id");

    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * 服务商
     */
    public final QueryColumn SERVICE = new QueryColumn(this, "service");

    public final QueryColumn VERSION = new QueryColumn(this, "version");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 文件名
     */
    public final QueryColumn FILE_NAME = new QueryColumn(this, "file_name");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 文件后缀名
     */
    public final QueryColumn FILE_SUFFIX = new QueryColumn(this, "file_suffix");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 原名
     */
    public final QueryColumn ORIGINAL_NAME = new QueryColumn(this, "original_name");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{URL, OSS_ID, SERVICE, VERSION, CREATE_BY, FILE_NAME, UPDATE_BY, CREATE_TIME, FILE_SUFFIX, UPDATE_TIME, ORIGINAL_NAME};

    public SysOssDef() {
        super("", "sys_oss");
    }

}
