package com.blank.common.web.interceptor

import cn.hutool.core.io.IoUtil
import cn.hutool.core.map.MapUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.json.utils.JsonUtils.toJsonString
import com.blank.common.web.filter.RepeatedlyRequestWrapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.StopWatch
import org.springframework.http.MediaType
import org.springframework.lang.Nullable
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView


/**
 * web的调用时间统计拦截器
 * dev环境有效
 */
@Slf4j
class PlusWebInvokeTimeInterceptor : HandlerInterceptor {

    private val prodProfile = "prod"

    private val invokeTimeTL = ThreadLocal<StopWatch>()

    @Throws(Exception::class)
    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (prodProfile != SpringUtil.getActiveProfile()) {
            val url = request.method + " " + request.requestURI

            // 打印请求参数
            if (isJsonRequest(request)) {
                var jsonParam: String? = ""
                if (request is RepeatedlyRequestWrapper) {
                    val reader = request.reader
                    jsonParam = IoUtil.read(reader)
                }
                log.debug {
                    "[PLUS]开始请求 => URL[$url],参数类型[json],参数:[$jsonParam]"
                }
            } else {
                val parameterMap = request.parameterMap
                if (MapUtil.isNotEmpty(parameterMap)) {
                    val parameters = toJsonString(parameterMap)
                    log.debug {
                        "[PLUS]开始请求 => URL[$url],参数类型[param],参数:[$parameters]"
                    }
                } else {
                    log.debug { "[PLUS]开始请求 => URL[$url],无参数" }
                }
            }
            val stopWatch = StopWatch()
            invokeTimeTL.set(stopWatch)
            stopWatch.start()
        }
        return true
    }

    @Throws(Exception::class)
    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        @Nullable modelAndView: ModelAndView?
    ) {
    }

    @Throws(Exception::class)
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        @Nullable ex: java.lang.Exception?
    ) {
        if (prodProfile != SpringUtil.getActiveProfile()) {
            val stopWatch = invokeTimeTL.get()
            stopWatch.stop()
            log.debug {
                "[PLUS]结束请求 => URL[${request.method} ${request.requestURI}],耗时:[${stopWatch.time}]毫秒"
            }
            invokeTimeTL.remove()
        }
    }

    /**
     * 判断本次请求的数据类型是否为json
     *
     * @param request request
     * @return boolean
     */
    private fun isJsonRequest(request: HttpServletRequest): Boolean {
        val contentType = request.contentType
        return if (contentType != null) {
            StringUtils.startsWithIgnoreCase(contentType, MediaType.APPLICATION_JSON_VALUE)
        } else false
    }
}
