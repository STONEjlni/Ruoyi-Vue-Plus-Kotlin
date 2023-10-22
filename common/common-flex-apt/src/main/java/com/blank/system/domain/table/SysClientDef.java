package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysClientDef extends TableDef {

    /**
     * 授权管理对象 sys_client
     */
    public static final SysClientDef SYS_CLIENT = new SysClientDef();

    /**
     * id
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 状态（0正常 1停用）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * token固定超时时间
     */
    public final QueryColumn TIMEOUT = new QueryColumn(this, "timeout");

    /**
     * 客户端id
     */
    public final QueryColumn CLIENT_ID = new QueryColumn(this, "client_id");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 客户端key
     */
    public final QueryColumn CLIENT_KEY = new QueryColumn(this, "client_key");

    /**
     * 授权类型
     */
    public final QueryColumn GRANT_TYPE = new QueryColumn(this, "grant_type");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 设备类型
     */
    public final QueryColumn DEVICE_TYPE = new QueryColumn(this, "device_type");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 客户端秘钥
     */
    public final QueryColumn CLIENT_SECRET = new QueryColumn(this, "client_secret");

    /**
     * token活跃超时时间
     */
    public final QueryColumn ACTIVE_TIMEOUT = new QueryColumn(this, "active_timeout");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, STATUS, TIMEOUT, CLIENT_ID, CREATE_BY, UPDATE_BY, CLIENT_KEY, GRANT_TYPE, CREATE_DEPT, CREATE_TIME, DEVICE_TYPE, UPDATE_TIME, CLIENT_SECRET, ACTIVE_TIMEOUT};

    public SysClientDef() {
        super("", "sys_client");
    }

}
