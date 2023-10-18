package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysRoleMenuDef extends TableDef {

    /**
     * 角色和菜单关联 sys_role_menu
     */
    public static final SysRoleMenuDef SYS_ROLE_MENU = new SysRoleMenuDef();

    /**
     * 菜单ID
     */
    public final QueryColumn MENU_ID = new QueryColumn(this, "menu_id");

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
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{MENU_ID, ROLE_ID};

    public SysRoleMenuDef() {
        super("", "sys_role_menu");
    }

}
