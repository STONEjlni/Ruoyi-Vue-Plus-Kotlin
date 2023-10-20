package com.blank.common.mybatis.core.mapper

import cn.hutool.core.util.StrUtil
import com.blank.common.mybatis.annotation.DataPermission
import com.blank.common.mybatis.core.page.PageQuery
import com.mybatisflex.core.BaseMapper
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper
import com.mybatisflex.core.update.UpdateChain

/**
 * 自定义 Mapper 接口, 实现 自定义扩展
 *
 * @param <T> table 泛型
 * @param <V> vo 泛型
 */
interface BaseMapperPlus<T> : BaseMapper<T> {
    fun <V> selectOneByQueryAs(
        queryWrapper: QueryWrapper,
        asType: Class<V>,
        dataPermission: DataPermission
    ): V {
        dataPermission.handler(queryWrapper)
        return this.selectOneByQueryAs(queryWrapper, asType)
    }

    fun <V> selectOneWithRelationsByQueryAs(
        queryWrapper: QueryWrapper,
        asType: Class<V>,
        dataPermission: DataPermission
    ): V {
        dataPermission.handler(queryWrapper)
        return this.selectOneWithRelationsByQueryAs(queryWrapper, asType)
    }


    fun <V> paginateAs(
        page: Page<V>,
        queryWrapper: QueryWrapper,
        asType: Class<V>,
        dataPermission: DataPermission
    ): Page<V> {
        dataPermission.handler(queryWrapper)
        return this.paginateAs(page, queryWrapper, asType)
    }

    fun <V> paginateAs(
        pageQuery: PageQuery,
        queryWrapper: QueryWrapper,
        asType: Class<V>,
        dataPermission: DataPermission
    ): Page<V> {
        dataPermission.handler(queryWrapper)
        return this.paginateAs(pageQuery.build(), queryWrapper, asType)
    }


    fun <V> selectListByQueryAs(
        queryWrapper: QueryWrapper,
        asType: Class<V>,
        dataPermission: DataPermission
    ): MutableList<V> {
        dataPermission.handler(queryWrapper)
        return this.selectListByQueryAs(queryWrapper, asType)
    }


    fun <V> paginateAs(pageQuery: PageQuery, queryWrapper: QueryWrapper, asType: Class<V>): Page<V> {
        queryWrapper.orderBy(*pageQuery.buildOrderBy())
        return this.paginateAs(pageQuery.build(), queryWrapper, asType)
    }

    fun paginate(pageQuery: PageQuery, queryWrapper: QueryWrapper): Page<T> {
        queryWrapper.orderBy(*pageQuery.buildOrderBy())
        return this.paginate(pageQuery.build(), queryWrapper)
    }


    fun update(updateChain: UpdateChain<T>, dataPermission: DataPermission): Boolean {
        val sql = dataPermission.toSQL(false)
        if (StrUtil.isNotBlank(sql)) {
            updateChain.and(sql)
        }
        return updateChain.update()
    }

    fun update(entity: T, dataPermission: DataPermission): Int {
        val sql = dataPermission.toSQL(false)
        if (StrUtil.isBlank(sql)) {
            return this.update(entity)
        }
        val queryWrapper = QueryWrapper.create().where(sql)
        return updateByQuery(entity, queryWrapper)
    }
}
