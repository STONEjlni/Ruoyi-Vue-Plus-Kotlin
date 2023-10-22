package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysNoticeDef extends TableDef {

    /**
     * 通知公告表 sys_notice
     */
    public static final SysNoticeDef SYS_NOTICE = new SysNoticeDef();

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 公告状态（0正常 1关闭）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 公告ID
     */
    public final QueryColumn NOTICE_ID = new QueryColumn(this, "notice_id");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 公告类型（1通知 2公告）
     */
    public final QueryColumn NOTICE_TYPE = new QueryColumn(this, "notice_type");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 公告标题
     */
    public final QueryColumn NOTICE_TITLE = new QueryColumn(this, "notice_title");

    /**
     * 公告内容
     */
    public final QueryColumn NOTICE_CONTENT = new QueryColumn(this, "notice_content");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{REMARK, STATUS, CREATE_BY, NOTICE_ID, UPDATE_BY, CREATE_DEPT, CREATE_TIME, NOTICE_TYPE, UPDATE_TIME, NOTICE_TITLE, NOTICE_CONTENT};

    public SysNoticeDef() {
        super("", "sys_notice");
    }

}
