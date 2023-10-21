package com.blank.common.mybatis.handler

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.domain.model.LoginUser
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.StreamUtils
import com.blank.common.mybatis.annotation.DataColumn
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.enums.DataScopeType
import com.blank.common.mybatis.helper.DataPermissionHelper
import com.blank.common.satoken.utils.LoginHelper
import com.mybatisflex.core.query.QueryWrapper
import org.springframework.context.expression.BeanFactoryResolver
import org.springframework.expression.BeanResolver
import org.springframework.expression.ExpressionParser
import org.springframework.expression.ParserContext
import org.springframework.expression.common.TemplateParserContext
import org.springframework.expression.spel.standard.SpelExpressionParser
import org.springframework.expression.spel.support.StandardEvaluationContext
import org.springframework.stereotype.Component
import java.util.function.Function

/**
 * 数据权限处理器
 */
@Slf4j
@Component
class PlusDataPermissionHandler {
    /**
     * spel 解析器
     */
    private val parser: ExpressionParser = SpelExpressionParser()
    private val parserContext: ParserContext = TemplateParserContext()

    /**
     * bean解析器 用于处理 spel 表达式中对 bean 的调用
     */
    private val beanResolver: BeanResolver = BeanFactoryResolver(SpringUtil.getBeanFactory())
    fun handlerDataPermission(dataPermission: DataPermission?, queryWrapper: QueryWrapper, isSelect: Boolean) {
        if (dataPermission == null) {
            return
        }
        val dataColumns: Array<DataColumn> = dataPermission.value
        if (ArrayUtil.isEmpty(dataColumns)) {
            return
        }
        var currentUser: LoginUser? = DataPermissionHelper.getVariable("user")
        if (ObjectUtil.isNull(currentUser)) {
            currentUser = LoginHelper.getLoginUser()!!
            DataPermissionHelper.setVariable("user", currentUser)
        }
        // 如果是超级管理员或租户管理员，则不过滤数据
        if (LoginHelper.isSuperAdmin()) {
            return
        }
        val dataFilterSql = buildDataFilter(dataColumns, isSelect)
        if (StrUtil.isBlank(dataFilterSql)) {
            return
        }
        queryWrapper.and(dataFilterSql)
    }

    fun getSQL(dataPermission: DataPermission, isSelect: Boolean): String {
        return buildDataFilter(dataPermission.value, isSelect)
    }

    /**
     * 构造数据过滤sql
     */
    private fun buildDataFilter(dataColumns: Array<DataColumn>, isSelect: Boolean): String {
        // 更新或删除需满足所有条件
        val joinStr = if (isSelect) " OR " else " AND "
        val user: LoginUser? = DataPermissionHelper.getVariable("user")
        val context = StandardEvaluationContext()
        context.setBeanResolver(beanResolver)
        DataPermissionHelper.getContext().forEach(context::setVariable)
        val conditions: MutableSet<String?> = HashSet()
        for (role in user!!.roles!!) {
            user.roleId = role.roleId
            // 获取角色权限泛型
            val type: DataScopeType? = DataScopeType.findCode(role.dataScope)
            if (ObjectUtil.isNull(type)) {
                throw ServiceException("角色数据范围异常 => " + role.dataScope)
            }
            // 全部数据权限直接返回
            if (type === DataScopeType.ALL) {
                return ""
            }
            var isSuccess = false
            for (dataColumn in dataColumns) {
                if (dataColumn.key.size != dataColumn.value.size) {
                    throw ServiceException("角色数据范围异常 => getKey与getValue长度不匹配")
                }
                // 不包含 getKey 变量 则不处理
                if (!StrUtil.containsAny(
                        type?.sqlTemplate,
                        *dataColumn.key.map { getKey -> "#$getKey" }.toTypedArray<String>()
                    )
                ) {
                    continue
                }
                // 设置注解变量 getKey 为表达式变量 getValue 为变量值
                for (i in 0 until dataColumn.key.size) {
                    context.setVariable(dataColumn.key[i], dataColumn.value[i])
                }

                // 解析sql模板并填充
                val sql = parser.parseExpression(type!!.sqlTemplate, parserContext).getValue(
                    context,
                    String::class.java
                )
                conditions.add(joinStr + sql)
                isSuccess = true
            }
            // 未处理成功则填充兜底方案
            if (!isSuccess && StrUtil.isNotBlank(type!!.elseSql)) {
                conditions.add(joinStr + type.elseSql)
            }
        }
        if (CollUtil.isNotEmpty(conditions)) {
            val sql: String = StreamUtils.join(conditions, Function.identity(), "")
            return sql.substring(joinStr.length)
        }
        return ""
    }
}

