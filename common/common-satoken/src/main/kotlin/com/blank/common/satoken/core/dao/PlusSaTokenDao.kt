package com.blank.common.satoken.core.dao

import cn.dev33.satoken.dao.SaTokenDao
import cn.dev33.satoken.util.SaFoxUtil
import com.blank.common.redis.utils.RedisUtils
import com.blank.common.redis.utils.RedisUtils.expire
import com.blank.common.redis.utils.RedisUtils.getCacheObject
import com.blank.common.redis.utils.RedisUtils.getTimeToLive
import com.blank.common.redis.utils.RedisUtils.keys
import com.blank.common.redis.utils.RedisUtils.setCacheObject
import java.time.Duration

/**
 * Sa-Token持久层接口(使用框架自带RedisUtils实现 协议统一)
 */
class PlusSaTokenDao : SaTokenDao {
    /**
     * 获取Value，如无返空
     */
    override fun get(key: String): String? {
        return getCacheObject(key)
    }

    /**
     * 写入Value，并设定存活时间 (单位: 秒)
     */
    override fun set(key: String, value: String, timeout: Long) {
        if (timeout == 0L || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return
        }
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            setCacheObject(key, value)
        } else {
            setCacheObject(key, value, Duration.ofSeconds(timeout))
        }
    }

    /**
     * 修修改指定key-value键值对 (过期时间不变)
     */
    override fun update(key: String, value: String) {
        val expire = getTimeout(key)
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return
        }
        this[key, value] = expire
    }

    /**
     * 删除Value
     */
    override fun delete(key: String) {
        RedisUtils.deleteObject(key)
    }

    /**
     * 获取Value的剩余存活时间 (单位: 秒)
     */
    override fun getTimeout(key: String): Long {
        val timeout = getTimeToLive<Any>(key)
        return if (timeout < 0) timeout else timeout / 1000
    }

    /**
     * 修改Value的剩余存活时间 (单位: 秒)
     */
    override fun updateTimeout(key: String, timeout: Long) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            val expire = getTimeout(key)
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                // 如果其已经被设置为永久，则不作任何处理
            } else {
                // 如果尚未被设置为永久，那么再次set一次
                this[key, this[key]!!] = timeout
            }
            return
        }
        expire(key, Duration.ofSeconds(timeout))
    }


    /**
     * 获取Object，如无返空
     */
    override fun getObject(key: String): Any? {
        return getCacheObject(key)
    }

    /**
     * 写入Object，并设定存活时间 (单位: 秒)
     */
    override fun setObject(key: String, `object`: Any?, timeout: Long) {
        if (timeout == 0L || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return
        }
        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            setCacheObject(key, `object`)
        } else {
            setCacheObject(key, `object`, Duration.ofSeconds(timeout))
        }
    }

    /**
     * 更新Object (过期时间不变)
     */
    override fun updateObject(key: String, `object`: Any) {
        val expire = getObjectTimeout(key)
        // -2 = 无此键
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return
        }
        setObject(key, `object`, expire)
    }

    /**
     * 删除Object
     */
    override fun deleteObject(key: String) {
        RedisUtils.deleteObject(key)
    }

    /**
     * 获取Object的剩余存活时间 (单位: 秒)
     */
    override fun getObjectTimeout(key: String): Long {
        val timeout = getTimeToLive<Any>(key)
        return if (timeout < 0) timeout else timeout / 1000
    }

    /**
     * 修改Object的剩余存活时间 (单位: 秒)
     */
    override fun updateObjectTimeout(key: String, timeout: Long) {
        // 判断是否想要设置为永久
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            val expire = getObjectTimeout(key)
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                // 如果其已经被设置为永久，则不作任何处理
            } else {
                // 如果尚未被设置为永久，那么再次set一次
                setObject(key, getObject(key), timeout)
            }
            return
        }
        expire(key, Duration.ofSeconds(timeout))
    }


    /**
     * 搜索数据
     */
    override fun searchData(prefix: String, keyword: String, start: Int, size: Int, sortType: Boolean): List<String> {
        val keys = keys("$prefix*$keyword*")
        val list: List<String> = ArrayList(keys)
        return SaFoxUtil.searchList(list, start, size, sortType)
    }
}
