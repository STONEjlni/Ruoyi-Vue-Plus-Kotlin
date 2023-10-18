package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysSocialDef extends TableDef {

    /**
     * 社会化关系对象 sys_social
     */
    public static final SysSocialDef SYS_SOCIAL = new SysSocialDef();

    /**
     * 主键
     */
    public final QueryColumn ID = new QueryColumn(this, "id");

    /**
     * 用户的授权code，部分平台可能没有
     */
    public final QueryColumn CODE = new QueryColumn(this, "code");

    /**
     * 授权的第三方邮箱
     */
    public final QueryColumn EMAIL = new QueryColumn(this, "email");

    /**
     * 授予的权限，部分平台可能没有
     */
    public final QueryColumn SCOPE = new QueryColumn(this, "scope");

    /**
     * 的唯一ID
     */
    public final QueryColumn AUTH_ID = new QueryColumn(this, "auth_id");

    /**
     * 授权的第三方头像地址
     */
    public final QueryColumn AVATAR = new QueryColumn(this, "avatar");

    /**
     * 小米平台用户的附带属性，部分平台可能没有
     */
    public final QueryColumn MAC_KEY = new QueryColumn(this, "mac_key");

    /**
     * 用户的 open id
     */
    public final QueryColumn OPEN_ID = new QueryColumn(this, "open_id");

    /**
     * 用户来源
     */
    public final QueryColumn SOURCE = new QueryColumn(this, "source");

    /**
     * 用户ID
     */
    public final QueryColumn USER_ID = new QueryColumn(this, "user_id");

    public final QueryColumn DEL_FLAG = new QueryColumn(this, "del_flag");

    /**
     * id token，部分平台可能没有
     */
    public final QueryColumn ID_TOKEN = new QueryColumn(this, "id_token");

    /**
     * 用户的 unionid
     */
    public final QueryColumn UNION_ID = new QueryColumn(this, "union_id");

    public final QueryColumn VERSION = new QueryColumn(this, "version");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 用户的授权令牌的有效期，部分平台可能没有
     */
    public final QueryColumn EXPIRE_IN = new QueryColumn(this, "expire_in");

    /**
     * 授权的第三方昵称
     */
    public final QueryColumn NICK_NAME = new QueryColumn(this, "nick_name");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * 授权的第三方账号
     */
    public final QueryColumn USER_NAME = new QueryColumn(this, "user_name");

    /**
     * 个别平台的授权信息，部分平台可能没有
     */
    public final QueryColumn TOKEN_TYPE = new QueryColumn(this, "token_type");

    /**
     * 平台的授权信息，部分平台可能没有
     */
    public final QueryColumn ACCESS_CODE = new QueryColumn(this, "access_code");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    /**
     * Twitter平台用户的附带属性，部分平台可能没有
     */
    public final QueryColumn OAUTH_TOKEN = new QueryColumn(this, "oauth_token");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 用户的授权令牌
     */
    public final QueryColumn ACCESS_TOKEN = new QueryColumn(this, "access_token");

    /**
     * 小米平台用户的附带属性，部分平台可能没有
     */
    public final QueryColumn MAC_ALGORITHM = new QueryColumn(this, "mac_algorithm");

    /**
     * 刷新令牌，部分平台可能没有
     */
    public final QueryColumn REFRESH_TOKEN = new QueryColumn(this, "refresh_token");

    /**
     * Twitter平台用户的附带属性，部分平台可能没有
     */
    public final QueryColumn OAUTH_TOKEN_SECRET = new QueryColumn(this, "oauth_token_secret");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{ID, CODE, EMAIL, SCOPE, AUTH_ID, AVATAR, MAC_KEY, OPEN_ID, SOURCE, USER_ID, ID_TOKEN, UNION_ID, VERSION, CREATE_BY, EXPIRE_IN, NICK_NAME, UPDATE_BY, USER_NAME, TOKEN_TYPE, ACCESS_CODE, CREATE_TIME, OAUTH_TOKEN, UPDATE_TIME, ACCESS_TOKEN, MAC_ALGORITHM, REFRESH_TOKEN, OAUTH_TOKEN_SECRET};

    public SysSocialDef() {
        super("", "sys_social");
    }

}
