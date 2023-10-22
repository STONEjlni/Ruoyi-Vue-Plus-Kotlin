package com.blank.system.domain.table;

import com.mybatisflex.core.query.QueryColumn;
import com.mybatisflex.core.table.TableDef;

// Auto generate by mybatis-flex, do not modify it.
public class SysOssConfigDef extends TableDef {

    /**
     * 对象存储配置对象 sys_oss_config
     */
    public static final SysOssConfigDef SYS_OSS_CONFIG = new SysOssConfigDef();

    /**
     * 扩展字段
     */
    public final QueryColumn EXT1 = new QueryColumn(this, "ext1");

    /**
     * 自定义域名
     */
    public final QueryColumn DOMAIN = new QueryColumn(this, "domain");

    /**
     * 前缀
     */
    public final QueryColumn PREFIX = new QueryColumn(this, "prefix");

    /**
     * 域
     */
    public final QueryColumn REGION = new QueryColumn(this, "region");

    /**
     * 备注
     */
    public final QueryColumn REMARK = new QueryColumn(this, "remark");

    /**
     * 是否默认（0=是,1=否）
     */
    public final QueryColumn STATUS = new QueryColumn(this, "status");

    /**
     * 是否https（0否 1是）
     */
    public final QueryColumn IS_HTTPS = new QueryColumn(this, "is_https");

    public final QueryColumn CREATE_BY = new QueryColumn(this, "create_by");

    /**
     * 访问站点
     */
    public final QueryColumn ENDPOINT = new QueryColumn(this, "endpoint");

    public final QueryColumn UPDATE_BY = new QueryColumn(this, "update_by");

    /**
     * accessKey
     */
    public final QueryColumn ACCESS_KEY = new QueryColumn(this, "access_key");

    /**
     * 配置key
     */
    public final QueryColumn CONFIG_KEY = new QueryColumn(this, "config_key");

    /**
     * 秘钥
     */
    public final QueryColumn SECRET_KEY = new QueryColumn(this, "secret_key");

    /**
     * 桶名称
     */
    public final QueryColumn BUCKET_NAME = new QueryColumn(this, "bucket_name");

    public final QueryColumn CREATE_DEPT = new QueryColumn(this, "create_dept");

    public final QueryColumn CREATE_TIME = new QueryColumn(this, "create_time");

    public final QueryColumn UPDATE_TIME = new QueryColumn(this, "update_time");

    /**
     * 主建
     */
    public final QueryColumn OSS_CONFIG_ID = new QueryColumn(this, "oss_config_id");

    /**
     * 桶权限类型(0private 1public 2custom)
     */
    public final QueryColumn ACCESS_POLICY = new QueryColumn(this, "access_policy");

    /**
     * 所有字段。
     */
    public final QueryColumn ALL_COLUMNS = new QueryColumn(this, "*");

    /**
     * 默认字段，不包含逻辑删除或者 large 等字段。
     */
    public final QueryColumn[] DEFAULT_COLUMNS = new QueryColumn[]{EXT1, DOMAIN, PREFIX, REGION, REMARK, STATUS, IS_HTTPS, CREATE_BY, ENDPOINT, UPDATE_BY, ACCESS_KEY, CONFIG_KEY, SECRET_KEY, BUCKET_NAME, CREATE_DEPT, CREATE_TIME, UPDATE_TIME, OSS_CONFIG_ID, ACCESS_POLICY};

    public SysOssConfigDef() {
        super("", "sys_oss_config");
    }

}
