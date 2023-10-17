package com.blank.common.mybatis.core.mapper

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.utils.MapstructUtils.convert
import com.blank.common.mybatis.toolkit.ReflectionKit
import com.mybatisflex.core.BaseMapper
import com.mybatisflex.core.paginate.Page
import com.mybatisflex.core.query.QueryWrapper
import java.io.Serializable
import java.util.*
import java.util.function.Function
import java.util.stream.Collectors

/**
 * 自定义 Mapper 接口, 实现 自定义扩展
 *
 * @param <T> table 泛型
 * @param <V> vo 泛型
 */
interface BaseMapperPlus<T, V> : BaseMapper<T> {
    fun currentVoClass(): Class<V> {
        return ReflectionKit.getSuperClassGenericType(
            this.javaClass,
            BaseMapperPlus::class.java, 1
        ) as Class<V>
    }

    fun currentModelClass(): Class<T> {
        return ReflectionKit.getSuperClassGenericType(
            this.javaClass,
            BaseMapperPlus::class.java, 0
        ) as Class<T>
    }

    fun selectList(): MutableList<T> {
        return this.selectListByQuery(QueryWrapper())
    }

    /**
     * 批量更新
     */
    fun updateBatchById(entityList: MutableCollection<T>): Boolean {
        if (CollUtil.isEmpty(entityList)) {
            return false
        }
        for (t in entityList) {
            this.update(t, true)
        }
        return true
    }

    /**
     * 批量插入或更新
     */
    fun insertOrUpdateBatch(entityList: MutableCollection<T>): Boolean {
        if (CollUtil.isEmpty(entityList)) {
            return false
        }
        for (t in entityList) {
            this.insertOrUpdate(t, true)
        }
        return true
    }

    fun selectVoById(id: Serializable): V? {
        return selectVoById(id, currentVoClass())
    }

    /**
     * 根据 ID 查询
     */
    fun <C> selectVoById(id: Serializable, voClass: Class<C>): C? {
        val obj = selectOneById(id)
        return if (ObjectUtil.isNull(obj)) {
            null
        } else convert(obj, voClass)
    }

    fun selectVoBatchIds(idList: MutableCollection<Serializable>): MutableList<V>? {
        return selectVoBatchIds(idList, currentVoClass())
    }

    /**
     * 查询（根据ID 批量查询）
     */
    fun <C> selectVoBatchIds(idList: MutableCollection<Serializable>, voClass: Class<C>): MutableList<C>? {
        val list = selectListByIds(idList)
        return if (CollUtil.isEmpty(list)) {
            CollUtil.newArrayList()
        } else convert<T, C>(list, voClass)
    }

    fun selectVoByMap(map: MutableMap<String, Any>): List<V>? {
        return selectVoByMap(map, currentVoClass())
    }

    /**
     * 查询（根据 columnMap 条件）
     */
    fun <C> selectVoByMap(map: MutableMap<String, Any>, voClass: Class<C>): List<C>? {
        val list = this.selectListByMap(map)
        return if (CollUtil.isEmpty(list)) {
            CollUtil.newArrayList()
        } else convert<T, C>(list, voClass)
    }

    fun selectVoOne(wrapper: QueryWrapper): V? {
        return selectVoOne(wrapper, currentVoClass())
    }

    /**
     * 根据 entity 条件，查询一条记录
     */
    fun <C> selectVoOne(wrapper: QueryWrapper, voClass: Class<C>): C? {
        val obj = selectOneByQuery(wrapper)
        return if (ObjectUtil.isNull(obj)) {
            null
        } else convert(obj, voClass)
    }

    fun selectVoList(): MutableList<V>? {
        return selectVoList(QueryWrapper(), currentVoClass())
    }

    fun selectVoList(wrapper: QueryWrapper): MutableList<V>? {
        return selectVoList(wrapper, currentVoClass())
    }

    /**
     * 根据 entity 条件，查询全部记录
     */
    fun <C> selectVoList(wrapper: QueryWrapper, voClass: Class<C>): MutableList<C>? {
        val list = this.selectListByQuery(wrapper)
        return if (CollUtil.isEmpty(list)) {
            CollUtil.newArrayList()
        } else convert<T, C>(list, voClass)
    }

    fun <P : Page<V>> selectVoPage(page: Page<T>, wrapper: QueryWrapper): P {
        return selectVoPage(page, wrapper, currentVoClass())
    }

    /**
     * 分页查询VO
     */
    fun <C, P : Page<C>> selectVoPage(page: Page<T>, wrapper: QueryWrapper, voClass: Class<C>): P {
        val list = this.paginate(page, wrapper)
        val voPage = Page<C>(page.pageNumber, page.pageSize, page.totalPage)
        if (!list.hasNext()) {
            return voPage as P
        }
        voPage.records = convert<T, C>(list.records, voClass)
        return voPage as P
    }

    fun <C> selectObjs(wrapper: QueryWrapper, mapper: Function<in Any?, C>): List<C> {
        return this.selectListByQuery(wrapper).stream().filter { obj: T ->
            Objects.nonNull(
                obj
            )
        }.map(mapper).collect(Collectors.toList())
    }
}
