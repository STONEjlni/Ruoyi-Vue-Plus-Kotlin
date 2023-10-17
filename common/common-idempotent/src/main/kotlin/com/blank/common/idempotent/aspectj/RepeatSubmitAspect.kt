package com.blank.common.idempotent.aspectj

import cn.dev33.satoken.SaManager
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.crypto.SecureUtil
import com.blank.common.core.constant.GlobalConstants
import com.blank.common.core.domain.R
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.utils.JakartaServletUtilExtend.getRequest
import com.blank.common.core.utils.MessageUtils.message
import com.blank.common.idempotent.annotation.RepeatSubmit
import com.blank.common.json.utils.JsonUtils
import com.blank.common.redis.utils.RedisUtils
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.validation.BindingResult
import org.springframework.web.multipart.MultipartFile
import java.time.Duration
import java.util.*

/**
 * 防止重复提交(参考美团GTIS防重系统)
 */
@Aspect
class RepeatSubmitAspect {
    companion object {
        private val KEY_CACHE = ThreadLocal<String>()
    }

    @Before("@annotation(repeatSubmit)")
    @Throws(Throwable::class)
    fun doBefore(point: JoinPoint, repeatSubmit: RepeatSubmit) {
        // 如果注解不为0 则使用注解数值
        val interval = repeatSubmit.timeUnit.toMillis(repeatSubmit.interval.toLong())
        if (interval < 1000) {
            throw ServiceException("重复提交间隔时间不能小于'1'秒")
        }
        val request = getRequest()
        val nowParams = argsArrayToString(point.args)

        // 请求地址（作为存放cache的key值）
        val url = request!!.requestURI

        // 唯一值（没有消息头则使用请求地址）
        var submitKey = StringUtils.trimToEmpty(request.getHeader(SaManager.getConfig().tokenName))
        submitKey = SecureUtil.md5("$submitKey:$nowParams")
        // 唯一标识（指定key + url + 消息头）
        val cacheRepeatKey = GlobalConstants.REPEAT_SUBMIT_KEY + url + submitKey
        if (RedisUtils.setObjectIfAbsent(cacheRepeatKey, "", Duration.ofMillis(interval))) {
            KEY_CACHE.set(cacheRepeatKey)
        } else {
            var message = repeatSubmit.message
            if (StringUtils.startsWith(message, "{") && StringUtils.endsWith(message, "}")) {
                message = message(StringUtils.substring(message, 1, message.length - 1))
            }
            throw ServiceException(message)
        }
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(repeatSubmit)", returning = "jsonResult")
    fun doAfterReturning(joinPoint: JoinPoint?, repeatSubmit: RepeatSubmit?, jsonResult: Any) {
        if (jsonResult is R<*>) {
            try {
                // 成功则不删除redis数据 保证在有效时间内无法重复提交
                if (jsonResult.code == R.SUCCESS) {
                    return
                }
                RedisUtils.deleteObject(KEY_CACHE.get())
            } finally {
                KEY_CACHE.remove()
            }
        }
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(repeatSubmit)", throwing = "e")
    fun doAfterThrowing(joinPoint: JoinPoint?, repeatSubmit: RepeatSubmit?, e: Exception?) {
        RedisUtils.deleteObject(KEY_CACHE.get())
        KEY_CACHE.remove()
    }

    /**
     * 参数拼装
     */
    private fun argsArrayToString(paramsArray: Array<Any>): String {
        val params = StringJoiner(" ")
        if (ArrayUtil.isEmpty(paramsArray)) {
            return params.toString()
        }
        for (o in paramsArray) {
            if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                params.add(JsonUtils.toJsonString(o))
            }
        }
        return params.toString()
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    fun isFilterObject(o: Any): Boolean {
        val clazz: Class<*> = o.javaClass
        if (clazz.isArray) {
            return clazz.componentType.isAssignableFrom(MultipartFile::class.java)
        } else if (MutableCollection::class.java.isAssignableFrom(clazz)) {
            val collection = o as Collection<*>
            for (value in collection) {
                return value is MultipartFile
            }
        } else if (MutableMap::class.java.isAssignableFrom(clazz)) {
            val map = o as Map<*, *>
            for (value in map.values) {
                return value is MultipartFile
            }
        }
        return (o is MultipartFile || o is HttpServletRequest || o is HttpServletResponse
            || o is BindingResult)
    }
}
