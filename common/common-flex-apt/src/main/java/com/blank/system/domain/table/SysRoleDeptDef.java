package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysRoleDeptDef extends TableDef {

    /**
     * 角色和部门关联 sys_role_dept
     */
    public static final SysRoleDeptDef SYS_ROLE_DEPT = new SysRoleDeptDef();

    /**
     * 部门ID
     */
    public final QueryColumn DEPT_ID = new QueryColumn(this, "dept_id");

    /**
     * 角色ID
     */
    public final QueryColumn ROLE_ID = new QueryColumn(this, "role_id");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{DEPT_ID, ROLE_ID};

    public SysRoleDeptDef() {
        super("", "sys_role_dept");
    }

}
