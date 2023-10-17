package com.blank.common.log.aspect

import cn.hutool.core.map.MapUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.utils.JakartaServletUtilExtend.getClientIP
import com.blank.common.core.utils.JakartaServletUtilExtend.getParamMap
import com.blank.common.core.utils.JakartaServletUtilExtend.getRequest
import com.blank.common.core.utils.SpringUtilExtend.context
import com.blank.common.json.utils.JsonUtils.parseMap
import com.blank.common.json.utils.JsonUtils.toJsonString
import com.blank.common.log.annotation.Log
import com.blank.common.log.enums.BusinessStatus
import com.blank.common.log.event.OperLogEvent
import com.blank.common.satoken.utils.LoginHelper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.StopWatch
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.AfterReturning
import org.aspectj.lang.annotation.AfterThrowing
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.boot.autoconfigure.AutoConfiguration
import org.springframework.http.HttpMethod
import org.springframework.validation.BindingResult
import org.springframework.web.multipart.MultipartFile
import java.util.*

/**
 * 操作日志记录处理
 */
@Slf4j
@Aspect
@AutoConfiguration
class LogAspect {
    companion object {
        /**
         * 排除敏感属性字段
         */
        private val EXCLUDE_PROPERTIES = arrayOf("password", "oldPassword", "newPassword", "confirmPassword")


        /**
         * 计算操作消耗时间
         */
        private val TIME_THREADLOCAL = ThreadLocal<StopWatch>()
    }

    /**
     * 处理请求前执行
     */
    @Before(value = "@annotation(controllerLog)")
    fun boBefore(joinPoint: JoinPoint?, controllerLog: Log?) {
        val stopWatch = StopWatch()
        TIME_THREADLOCAL.set(stopWatch)
        stopWatch.start()
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    fun doAfterReturning(joinPoint: JoinPoint, controllerLog: Log, jsonResult: Any?) {
        handleLog(joinPoint, controllerLog, null, jsonResult)
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    fun doAfterThrowing(joinPoint: JoinPoint, controllerLog: Log, e: Exception?) {
        handleLog(joinPoint, controllerLog, e, null)
    }

    protected fun handleLog(joinPoint: JoinPoint, controllerLog: Log, e: Exception?, jsonResult: Any?) {
        try {

            // *========数据库日志=========*//
            val operLog = OperLogEvent()
            operLog.status = BusinessStatus.SUCCESS.ordinal
            // 请求的地址
            val ip = getClientIP()
            operLog.operIp = ip
            operLog.operUrl =
                StringUtils.substring(getRequest()!!.requestURI, 0, 255)
            operLog.operName = LoginHelper.getUsername()
            if (e != null) {
                operLog.status = BusinessStatus.FAIL.ordinal
                operLog.errorMsg = StringUtils.substring(e.message, 0, 2000)
            }
            // 设置方法名称
            val className = joinPoint.target.javaClass.getName()
            val methodName = joinPoint.signature.name
            operLog.method = "$className.$methodName()"
            // 设置请求方式
            operLog.requestMethod = getRequest()!!.method
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operLog, jsonResult)
            // 设置消耗时间
            val stopWatch = TIME_THREADLOCAL.get()
            stopWatch.stop()
            operLog.costTime = stopWatch.time
            // 发布事件保存数据库
            context().publishEvent(operLog)
        } catch (exp: Exception) {
            // 记录本地异常日志
            log.error { "异常信息:$${exp.message}" }
            exp.printStackTrace()
        } finally {
            TIME_THREADLOCAL.remove()
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log     日志
     * @param operLog 操作日志
     * @throws Exception
     */
    @Throws(Exception::class)
    fun getControllerMethodDescription(joinPoint: JoinPoint, log: Log, operLog: OperLogEvent, jsonResult: Any?) {
        // 设置action动作
        operLog.businessType = log.businessType.ordinal
        // 设置标题
        operLog.title = log.title
        // 设置操作人类别
        operLog.operatorType = log.operatorType.ordinal
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operLog, log.excludeParamNames)
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData && ObjectUtil.isNotNull(jsonResult)) {
            operLog.jsonResult = StringUtils.substring(toJsonString(jsonResult), 0, 2000)
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    @Throws(Exception::class)
    private fun setRequestValue(joinPoint: JoinPoint, operLog: OperLogEvent, excludeParamNames: Array<String>) {
        val paramsMap: Map<String, String> = getParamMap(getRequest()!!)
        val requestMethod = operLog.requestMethod
        if (MapUtil.isEmpty(paramsMap) && HttpMethod.PUT.name() == requestMethod || HttpMethod.POST.name() == requestMethod) {
            val params = argsArrayToString(joinPoint.args, excludeParamNames)
            operLog.operParam = StringUtils.substring(params, 0, 2000)
        } else {
            MapUtil.removeAny(paramsMap, *EXCLUDE_PROPERTIES)
            MapUtil.removeAny(paramsMap, *excludeParamNames)
            operLog.operParam = StringUtils.substring(toJsonString(paramsMap), 0, 2000)
        }
    }

    /**
     * 参数拼装
     */
    private fun argsArrayToString(paramsArray: Array<Any>, excludeParamNames: Array<String>): String {
        val params = StringJoiner(" ")
        if (ArrayUtil.isEmpty<Any>(paramsArray)) {
            return params.toString()
        }
        for (o in paramsArray) {
            if (ObjectUtil.isNotNull(o) && !isFilterObject(o)) {
                var str = toJsonString(o)
                val dict = parseMap(str)
                if (MapUtil.isNotEmpty(dict)) {
                    MapUtil.removeAny(dict, *EXCLUDE_PROPERTIES)
                    MapUtil.removeAny(dict, *excludeParamNames)
                    str = toJsonString(dict)
                }
                params.add(str)
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
