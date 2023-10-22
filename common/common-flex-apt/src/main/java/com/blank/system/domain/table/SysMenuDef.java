package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysMenuDef extends TableDef {

    /**
     * 菜单权限表 sys_menu
     */
    public static final SysMenuDef SYS_MENU = new SysMenuDef();

    /**
     * 菜单图标
     */
    public final QueryColumn ICON = new QueryColumn(this, "icon");

    /**
     * 路由地址
     */
    public final QueryColumn PATH = new QueryColumn(this, "path");

    /**
     * 权限字符串
     */
    public final QueryColumn PERMS = new QueryColumn(this, "perms");

    /**
     * 菜单ID
     */
    public final QueryColumn MENU_ID = new QueryColumn(this, "menu_id");

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 菜单状态（0正常 1停用）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 是否缓存（0缓存 1不缓存）
     */
    public final QueryColumn IS_CACHE = new QueryColumn(this, "is_cache");

    /**
     * 是否为外链（0是 1否）
     */
    public final QueryColumn IS_FRAME = new QueryColumn(this, "is_frame");

    /**
     * 显示状态（0显示 1隐藏）
     */
    public final QueryColumn VISIBLE = new QueryColumn(this, "visible");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 菜单名称
     */
    public final QueryColumn MENU_NAME = new QueryColumn(this, "menu_name");

    /**
     * 类型（M目录 C菜单 F按钮）
     */
    public final QueryColumn MENU_TYPE = new QueryColumn(this, "menu_type");

    /**
     * 显示顺序
     */
    public final QueryColumn ORDER_NUM = new QueryColumn(this, "order_num");

    /**
     * 父菜单ID
     */
    public final QueryColumn PARENT_ID = new QueryColumn(this, "parent_id");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 组件路径
     */
    public final QueryColumn COMPONENT = new QueryColumn(this, "component");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * 路由参数
     */
    public final QueryColumn QUERY_PARAM = new QueryColumn(this, "query_param");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ICON, PATH, PERMS, MENU_ID, REMARK, STATUS, IS_CACHE, IS_FRAME, VISIBLE, CREATE_BY, MENU_NAME, MENU_TYPE, ORDER_NUM, PARENT_ID, UPDATE_BY, COMPONENT, CREATE_DEPT, CREATE_TIME, QUERY_PARAM, UPDATE_TIME};

    public SysMenuDef() {
        super("", "sys_menu");
    }

}
