package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysLogininforDef extends TableDef {

    /**
     * 系统访问记录表 sys_logininfor
     */
    public static final SysLogininforDef SYS_LOGININFOR = new SysLogininforDef();

    /**
     * 操作系统
     */
    public final QueryColumn OS = new QueryColumn(this, "os");

    /**
     * 提示消息
     */
    public final QueryColumn MSG = new QueryColumn(this, "msg");

    /**
     * ID
     */
    public final QueryColumn INFO_ID = new QueryColumn(this, "info_id");

    /**
     * 登录IP地址
     */
    public final QueryColumn IPADDR = new QueryColumn(this, "ipaddr");

    /**
     * 登录状态 0成功 1失败
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 浏览器类型
     */
    public final QueryColumn BROWSER = new QueryColumn(this, "browser");

    /**
     * 用户账号
     */
    public final QueryColumn USER_NAME = new QueryColumn(this, "user_name");

    /**
     * 客户端
     */
    public final QueryColumn CLIENT_KEY = new QueryColumn(this, "client_key");

    /**
     * 访问时间
     */
    public final QueryColumn LOGIN_TIME = new QueryColumn(this, "login_time");

    /**
     * 设备类型
     */
    public final QueryColumn DEVICE_TYPE = new QueryColumn(this, "device_type");

    /**
     * 登录地点
     */
    public final QueryColumn LOGIN_LOCATION = new QueryColumn(this, "login_location");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{OS, MSG, INFO_ID, IPADDR, STATUS, BROWSER, USER_NAME, CLIENT_KEY, LOGIN_TIME, DEVICE_TYPE, LOGIN_LOCATION};

    public SysLogininforDef() {
        super("", "sys_logininfor");
    }

}
