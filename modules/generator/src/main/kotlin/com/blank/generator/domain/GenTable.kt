package com.blank.generator.domain

import com.blank.common.core.annotation.NoArg
import com.blank.common.core.annotation.Open
import com.blank.common.mybatis.core.domain.BaseEntity
import com.blank.generator.constant.GenConstants
import com.mybatisflex.annotation.Column
import com.mybatisflex.annotation.Id
import com.mybatisflex.annotation.RelationOneToMany
import com.mybatisflex.annotation.Table
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.apache.commons.lang3.StringUtils

/**
 * 代码生成表 gen_table
 */
@Table("gen_table")
@Open
@NoArg
class GenTable : BaseEntity() {

    /**
     * 编号
     */
    @Id
    var tableId: Long? = null

    /**
     * 数据源名称
     */
    var dataName: @NotBlank(message = "数据源名称不能为空") String? = null

    /**
     * 表名称
     */
    var tableName: @NotBlank(message = "表名称不能为空") String? = null

    /**
     * 表描述
     */
    var tableComment: @NotBlank(message = "表描述不能为空") String? = null

    /**
     * 关联父表的表名
     */
    var subTableName: String? = null

    /**
     * 本表关联父表的外键名
     */
    var subTableFkName: String? = null

    /**
     * 实体类名称(首字母大写)
     */
    var className: @NotBlank(message = "实体类名称不能为空") String? = null

    /**
     * 使用的模板（crud单表操作 tree树表操作 sub主子表操作）
     */
    var tplCategory: String? = null

    /**
     * 生成包路径
     */
    var packageName: @NotBlank(message = "生成包路径不能为空") String? = null

    /**
     * 生成模块名
     */
    var moduleName: @NotBlank(message = "生成模块名不能为空") String? = null

    /**
     * 生成业务名
     */
    var businessName: @NotBlank(message = "生成业务名不能为空") String? = null

    /**
     * 生成功能名
     */
    var functionName: @NotBlank(message = "生成功能名不能为空") String? = null

    /**
     * 生成作者
     */
    var functionAuthor: @NotBlank(message = "作者不能为空") String? = null

    /**
     * 生成代码方式（0zip压缩包 1自定义路径）
     */
    var genType: String? = null

    /**
     * 生成路径（不填默认项目路径）
     */
    var genPath: String? = null

    /**
     * 主键信息
     */
    @Column(ignore = true)
    var pkColumn: GenTableColumn? = null

    /**
     * 表列信息
     */
    @Valid
    @Column(ignore = true)
    @RelationOneToMany(selfField = "tableId", targetField = "tableId", orderBy = "sort")
    var columns: MutableList<GenTableColumn>? = null

    /**
     * 其它生成选项
     */
    var options: String? = null

    /**
     * 备注
     */
    var remark: String? = null

    /**
     * 树编码字段
     */
    @Column(ignore = true)
    var treeCode: String? = null

    /**
     * 树父编码字段
     */
    @Column(ignore = true)
    var treeParentCode: String? = null

    /**
     * 树名称字段
     */
    @Column(ignore = true)
    var treeName: String? = null

    /*
     * 菜单id列表
     */
    @Column(ignore = true)
    var menuIds: MutableList<Long>? = null

    /**
     * 上级菜单ID字段
     */
    @Column(ignore = true)
    var parentMenuId: String? = null

    /**
     * 上级菜单名称字段
     */
    @Column(ignore = true)
    var parentMenuName: String? = null

    fun isTree(): Boolean {
        return isTree(tplCategory)
    }

    fun isCrud(): Boolean {
        return isCrud(tplCategory)
    }

    fun isSuperColumn(javaField: String?): Boolean {
        return isSuperColumn(tplCategory, javaField)
    }

    companion object {
        fun isTree(tplCategory: String?): Boolean {
            return tplCategory != null && StringUtils.equals(GenConstants.TPL_TREE, tplCategory)
        }

        fun isCrud(tplCategory: String?): Boolean {
            return tplCategory != null && StringUtils.equals(GenConstants.TPL_CRUD, tplCategory)
        }

        fun isSuperColumn(tplCategory: String?, javaField: String?): Boolean {
            return StringUtils.equalsAnyIgnoreCase(javaField, *GenConstants.BASE_ENTITY)
        }
    }
}
