package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysUserDef extends TableDef {

    /**
     * 用户对象 sys_user
     */
    public static final SysUserDef SYS_USER = new SysUserDef();

    /**
     * 用户性别
     */
    public final QueryColumn SEX = new QueryColumn(this, "sex");

    /**
     * 用户邮箱
     */
    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    /**
     * 用户头像
     */
    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    /**
     * 部门ID
     */
    public final QueryColumn DEPT_ID = new QueryColumn(this, "dept_id");

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 帐号状态（0正常 1停用）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * 最后登录IP
     */
    public final QueryColumn LOGIN_IP = new QueryColumn(this, "login_ip");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 用户昵称
     */
    public final QueryColumn NICK_NAME = new QueryColumn(this, "nick_name");

    /**
     * 密码
     */
    public final QueryColumn PASSWORD = new QueryColumn(this, "password");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 用户账号
     */
    public final QueryColumn USER_NAME = new QueryColumn(this, "user_name");

    /**
     * 用户类型（sys_user系统用户）
     */
    public final QueryColumn USER_TYPE = new QueryColumn(this, "user_type");

    /**
     * 最后登录时间
     */
    public final QueryColumn LOGIN_DATE = new QueryColumn(this, "login_date");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 手机号码
     */
    public final QueryColumn PHONENUMBER = new QueryColumn(this, "phonenumber");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{SEX, EMAIL, AVATAR, DEPT_ID, REMARK, STATUS, USER_ID, LOGIN_IP, CREATE_BY, NICK_NAME, PASSWORD, UPDATE_BY, USER_NAME, USER_TYPE, LOGIN_DATE, CREATE_DEPT, CREATE_TIME, UPDATE_TIME, PHONENUMBER};

    public SysUserDef() {
        super("", "sys_user");
    }

}
