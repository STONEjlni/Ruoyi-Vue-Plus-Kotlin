package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysOperLogDef extends TableDef {

    /**
     * 操作日志记录表 oper_log
     */
    public static final SysOperLogDef SYS_OPER_LOG = new SysOperLogDef();

    /**
     * 操作模块
     */
    public final QueryColumn TITLE = new QueryColumn(this, "title");

    /**
     * 请求方法
     */
    public final QueryColumn METHOD = new QueryColumn(this, "method");

    /**
     * 日志主键
     */
    public final QueryColumn OPER_ID = new QueryColumn(this, "oper_id");

    /**
     * 操作地址
     */
    public final QueryColumn OPER_IP = new QueryColumn(this, "oper_ip");

    /**
     * 操作状态（0正常 1异常）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 请求url
     */
    public final QueryColumn OPER_URL = new QueryColumn(this, "oper_url");

    /**
     * 消耗时间
     */
    public final QueryColumn COST_TIME = new QueryColumn(this, "cost_time");

    /**
     * 部门名称
     */
    public final QueryColumn DEPT_NAME = new QueryColumn(this, "dept_name");

    /**
     * 错误消息
     */
    public final QueryColumn ERROR_MSG = new QueryColumn(this, "error_msg");

    /**
     * 操作人员
     */
    public final QueryColumn OPER_NAME = new QueryColumn(this, "oper_name");

    /**
     * 操作时间
     */
    public final QueryColumn OPER_TIME = new QueryColumn(this, "oper_time");

    /**
     * 请求参数
     */
    public final QueryColumn OPER_PARAM = new QueryColumn(this, "oper_param");

    /**
     * 返回参数
     */
    public final QueryColumn JSON_RESULT = new QueryColumn(this, "json_result");

    /**
     * 业务类型（0其它 1新增 2修改 3删除）
     */
    public final QueryColumn BUSINESS_TYPE = new QueryColumn(this, "business_type");

    /**
     * 操作地点
     */
    public final QueryColumn OPER_LOCATION = new QueryColumn(this, "oper_location");

    /**
     * 操作类别（0其它 1后台用户 2手机端用户）
     */
    public final QueryColumn OPERATOR_TYPE = new QueryColumn(this, "operator_type");

    /**
     * 请求方式
     */
    public final QueryColumn REQUEST_METHOD = new QueryColumn(this, "request_method");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{TITLE, METHOD, OPER_ID, OPER_IP, STATUS, OPER_URL, COST_TIME, DEPT_NAME, ERROR_MSG, OPER_NAME, OPER_TIME, OPER_PARAM, JSON_RESULT, BUSINESS_TYPE, OPER_LOCATION, OPERATOR_TYPE, REQUEST_METHOD};

    public SysOperLogDef() {
        super("", "sys_oper_log");
    }

}
