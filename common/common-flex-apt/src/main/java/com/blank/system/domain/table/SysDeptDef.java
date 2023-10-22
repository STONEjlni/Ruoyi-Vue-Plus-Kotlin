package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysDeptDef extends TableDef {

    /**
     * 部门表 sys_dept
     */
    public static final SysDeptDef SYS_DEPT = new SysDeptDef();

    /**
     * 邮箱
     */
    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    /**
     * 联系电话
     */
    public final QueryColumn PHONE = new QueryColumn(this, "phone");

    /**
     * 部门ID
     */
    public final QueryColumn DEPT_ID = new QueryColumn(this, "dept_id");

    /**
     * 负责人
     */
    public final QueryColumn LEADER = new QueryColumn(this, "leader");

    /**
     * 部门状态:0正常,1停用
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 部门名称
     */
    public final QueryColumn DEPT_NAME = new QueryColumn(this, "dept_name");

    /**
     * 显示顺序
     */
    public final QueryColumn ORDER_NUM = new QueryColumn(this, "order_num");

    /**
     * 父部门ID
     */
    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 祖级列表
     */
    public final QueryColumn ANCESTORS = new QueryColumn(this, "ancestors");

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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{EMAIL, PHONE, DEPT_ID, LEADER, STATUS, CREATE_BY, DEPT_NAME, ORDER_NUM, PARENT_ID, UPDATE_BY, ANCESTORS, CREATE_DEPT, CREATE_TIME, UPDATE_TIME};

    public SysDeptDef() {
        super("", "sys_dept");
    }

}
