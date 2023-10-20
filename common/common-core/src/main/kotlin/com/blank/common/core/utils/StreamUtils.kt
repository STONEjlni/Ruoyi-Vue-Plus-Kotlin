package com.blank.common.core.utils

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.map.MapUtil
import org.apache.commons.lang3.StringUtils
import java.util.*
import java.util.function.BiFunction
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Collectors

/**
 * stream 流工具类
 * https://segmentfault.com/q/1010000042901551
 */
object StreamUtils {
    /**
     * 将collection过滤
     *
     * @param collection 需要转化的集合
     * @param function   过滤方法
     * @return 过滤后的list
     */
    fun <E> filter(collection: Collection<E>, function: Predicate<E>): MutableList<E> {
        return if (CollUtil.isEmpty(collection)) {
            CollUtil.newArrayList()
        } else collection.filter { function.test(it) }.toMutableList()
        // collection.stream().filter(function).collect(Collectors.toList())
        // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
    }

    /**
     * 将collection拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @return 拼接后的list
     */
    fun <E> join(collection: Collection<E>, function: Function<E, String>): String {
        return join<E>(collection, function, StringUtilsExtend.SEPARATOR)
    }

    /**
     * 将collection拼接
     *
     * @param collection 需要转化的集合
     * @param function   拼接方法
     * @param delimiter  拼接符
     * @return 拼接后的list
     */
    fun <E> join(collection: Collection<E>, function: Function<E, String>, delimiter: CharSequence): String {
        return if (CollUtil.isEmpty(collection)) {
            StringUtils.EMPTY
        } else collection
            .map { function.apply(it) }
            .filter { Objects.nonNull(it) }
            .joinToString { delimiter }
        /*.collect(Collectors.joining(delimiter))*/
    }

    /**
     * 将collection排序
     *
     * @param collection 需要转化的集合
     * @param comparing  排序方法
     * @return 排序后的list
     */
    fun <E> sorted(collection: Collection<E>, comparing: Comparator<E>): List<E> {
        return if (CollUtil.isEmpty(collection)) {
            CollUtil.newArrayList()
        } else collection
            .filter { Objects.nonNull(it) }
            .sortedWith(comparing)
        /*collection.stream().filter { obj: E? -> Objects.nonNull(obj) }.sorted(comparing)
            .collect(Collectors.toList())*/
        // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
    }

    /**
     * 将collection转化为类型不变的map<br></br>
     * <B>`Collection<V>  ---->  Map<K,V>`</B>
     *
     * @param collection 需要转化的集合
     * @param key        V类型转化为K类型的lambda方法
     * @param <V>        collection中的泛型
     * @param <K>        map中的key类型
     * @return 转化后的map
    </K></V> */
    fun <V, K> toIdentityMap(collection: Collection<V>, key: Function<V, K>): Map<K, V> {
        return if (CollUtil.isEmpty(collection)) {
            MapUtil.newHashMap()
        } else collection
            .filter { Objects.nonNull(it) }
            .associateBy { key.apply(it) }

        /*collection.stream().filter { obj: V? -> Objects.nonNull(obj) }
            .collect(
                Collectors.toMap(key, Function.identity(),
                    BinaryOperator { l: V?, r: V? -> l })
            )*/
    }

    /**
     * 将Collection转化为map(value类型与collection的泛型不同)<br></br>
     * <B>`Collection<E> -----> Map<K,V>  `</B>
     *
     * @param collection 需要转化的集合
     * @param key        E类型转化为K类型的lambda方法
     * @param value      E类型转化为V类型的lambda方法
     * @param <E>        collection中的泛型
     * @param <K>        map中的key类型
     * @param <V>        map中的value类型
     * @return 转化后的map
    </V></K></E> */
    fun <E, K, V> toMap(collection: Collection<E>, key: Function<E, K>, value: Function<E, V>): Map<K, V> {
        return if (CollUtil.isEmpty(collection)) {
            MapUtil.newHashMap()
        } else collection.stream().filter { obj: E -> Objects.nonNull(obj) }
            .collect(
                Collectors.toMap(
                    key, value
                ) { l: V, _: V -> l }
            )
    }

    /**
     * 将collection按照规则(比如有相同的班级id)分类成map<br></br>
     * <B>`Collection<E> -------> Map<K,List<E>> ` </B>
     *
     * @param collection 需要分类的集合
     * @param key        分类的规则
     * @param <E>        collection中的泛型
     * @param <K>        map中的key类型
     * @return 分类后的map
    </K></E> */
    fun <E, K> groupByKey(collection: Collection<E>, key: Function<E, K>): Map<K, List<E>> {
        return if (CollUtil.isEmpty(collection)) {
            MapUtil.newHashMap()
        } else collection
            .filter { Objects.nonNull(it) }
            .groupBy { key.apply(it) }

        /*return collection
        .stream().filter(Objects::nonNull)
        .collect(Collectors.groupingBy(key,
            LinkedHashMap::new,
            Collectors.toList()));*/
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br></br>
     * <B>`Collection<E>  --->  Map<T,Map<U,List<E>>> ` </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <E>        集合元素类型
     * @param <K>        第一个map中的key类型
     * @param <U>        第二个map中的key类型
     * @return 分类后的map
    </U></K></E> */
    fun <E, K, U> groupBy2Key(
        collection: Collection<E>,
        key1: Function<E, K>,
        key2: Function<E, U>
    ): Map<K, Map<U, List<E>>> {
        return if (CollUtil.isEmpty(collection)) {
            MapUtil.newHashMap()
        } else collection
            .filter { Objects.nonNull(it) }
            .groupBy { key1.apply(it) }
            .mapValues {
                it.value.groupBy { key2.apply(it) }
            }


        /*collection
        .stream().filter(Objects::nonNull)
        .collect(Collectors.groupingBy(key1, LinkedHashMap::new,
        Collectors.groupingBy(key2, LinkedHashMap::new, Collectors.toList())));
        */
    }

    /**
     * 将collection按照两个规则(比如有相同的年级id,班级id)分类成双层map<br></br>
     * <B>`Collection<E>  --->  Map<T,Map<U,E>> ` </B>
     *
     * @param collection 需要分类的集合
     * @param key1       第一个分类的规则
     * @param key2       第二个分类的规则
     * @param <T>        第一个map中的key类型
     * @param <U>        第二个map中的key类型
     * @param <E>        collection中的泛型
     * @return 分类后的map
    </E></U></T> */
    fun <E, T, U> group2Map(
        collection: Collection<E>,
        key1: Function<E, T>,
        key2: Function<E, U>
    ): Map<T, Map<U, E>> {
        return if (CollUtil.isEmpty(collection)) {
            MapUtil.newHashMap()
        } else collection
            .filter { Objects.nonNull(it) }
            .groupBy { key1.apply(it) }
            .mapValues {
                it.value.associateBy {
                    key2.apply(it)
                }
            }
        /*.collect(
            Collectors.groupingBy<E?, T, Map<U, E?>, Any, java.util.LinkedHashMap<T, Map<U, E>>>(
                key1,
                Supplier { LinkedHashMap() }, Collectors.toMap(key2, Function.identity(),
                    BinaryOperator { l: E, r: E -> l })
            )
        )*/
    }

    /**
     * 将collection转化为List集合，但是两者的泛型不同<br></br>
     * <B>`Collection<E>  ------>  List<T> ` </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为list泛型的lambda表达式
     * @param <E>        collection中的泛型
     * @param <T>        List中的泛型
     * @return 转化后的list
    </T></E> */
    fun <E, T> toList(collection: Collection<E>, function: Function<E, T>): MutableList<T> {
        return if (CollUtil.isEmpty(collection)) {
            CollUtil.newArrayList()
        } else collection
            .map { function.apply(it) }
            .filter { Objects.nonNull(it) }
            .toMutableList()

        /*collection
        .stream()
        .map(function)
        .filter { obj: T -> Objects.nonNull(obj) } // 注意此处不要使用 .toList() 新语法 因为返回的是不可变List 会导致序列化问题
        .collect(Collectors.toList())*/
    }

    /**
     * 将collection转化为Set集合，但是两者的泛型不同<br></br>
     * <B>`Collection<E>  ------>  Set<T> ` </B>
     *
     * @param collection 需要转化的集合
     * @param function   collection中的泛型转化为set泛型的lambda表达式
     * @param <E>        collection中的泛型
     * @param <T>        Set中的泛型
     * @return 转化后的Set
    </T></E> */
    fun <E, T> toSet(collection: Collection<E>, function: Function<E, T>): Set<T> {
        return if (CollUtil.isEmpty(collection) || function == null) {
            CollUtil.newHashSet()
        } else collection
            .map { function.apply(it) }
            .filter { Objects.nonNull(it) }
            .toSet()

        /*collection
        .stream()
        .map(function)
        .filter { obj: T -> Objects.nonNull(obj) }
        .collect(Collectors.toSet())*/
    }


    /**
     * 合并两个相同key类型的map
     *
     * @param map1  第一个需要合并的 map
     * @param map2  第二个需要合并的 map
     * @param merge 合并的lambda，将key  value1 value2合并成最终的类型,注意value可能为空的情况
     * @param <K>   map中的key类型
     * @param <X>   第一个 map的value类型
     * @param <Y>   第二个 map的value类型s
     * @param <V>   最终map的value类型
     * @return 合并后的map
    </V></Y></X></K> */
    fun <K, X, Y, V> merge(map1: Map<K, X>, map2: Map<K, Y>, merge: BiFunction<X, Y, V>): Map<K, V> {
        var map1 = map1
        var map2 = map2

        if (MapUtil.isEmpty(map1) && MapUtil.isEmpty(map2)) {
            return MapUtil.newHashMap()
        } else if (MapUtil.isEmpty(map1)) {
            map1 = MapUtil.newHashMap()
        } else if (MapUtil.isEmpty(map2)) {
            map2 = MapUtil.newHashMap()
        }
        val key: MutableSet<K> = HashSet()
        key.addAll(map1.keys)
        key.addAll(map2.keys)
        val map: MutableMap<K, V> = HashMap()
        for (t in key) {
            val x = map1[t]
            val y = map2[t]
            val z: V = merge.apply(x!!, y!!)
            if (z != null) {
                map[t] = z
            }
        }
        return map
    }
}
