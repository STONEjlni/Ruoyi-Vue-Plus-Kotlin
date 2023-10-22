package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysRoleDef extends TableDef {

    /**
     * 角色表 sys_role
     */
    public static final SysRoleDef SYS_ROLE = new SysRoleDef();

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 角色ID
     */
    public final QueryColumn ROLE_ID = new QueryColumn(this, "role_id");

    /**
     * 角色状态（0正常 1停用）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * 角色权限
     */
    public final QueryColumn ROLE_KEY = new QueryColumn(this, "role_key");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 角色名称
     */
    public final QueryColumn ROLE_NAME = new QueryColumn(this, "role_name");

    /**
     * 角色排序
     */
    public final QueryColumn ROLE_SORT = new QueryColumn(this, "role_sort");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 数据范围（1：所有数据权限；2：自定义数据权限；3：本部门数据权限；4：本部门及以下数据权限；5：仅本人数据权限）
     */
    public final QueryColumn DATA_SCOPE = new QueryColumn(this, "data_scope");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 部门树选择项是否关联显示（0：父子不互相关联显示 1：父子互相关联显示 ）
     */
    public final QueryColumn DEPT_CHECK_STRICTLY = new QueryColumn(this, "dept_check_strictly");

    /**
     * 菜单树选择项是否关联显示（ 0：父子不互相关联显示 1：父子互相关联显示）
     */
    public final QueryColumn MENU_CHECK_STRICTLY = new QueryColumn(this, "menu_check_strictly");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{REMARK, ROLE_ID, STATUS, ROLE_KEY, CREATE_BY, ROLE_NAME, ROLE_SORT, UPDATE_BY, DATA_SCOPE, CREATE_DEPT, CREATE_TIME, UPDATE_TIME, DEPT_CHECK_STRICTLY, MENU_CHECK_STRICTLY};

    public SysRoleDef() {
        super("", "sys_role");
    }

}
