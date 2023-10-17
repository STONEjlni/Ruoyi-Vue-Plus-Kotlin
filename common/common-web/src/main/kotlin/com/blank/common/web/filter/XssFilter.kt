package com.blank.common.web.filter

import com.blank.common.core.utils.StringUtilsExtend
import com.blank.common.core.utils.StringUtilsExtend.matches
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpMethod
import java.io.IOException

/**
 * 防止XSS攻击的过滤器
 */
class XssFilter : Filter {
    /**
     * 排除链接
     */
    private var excludes: MutableList<String> = ArrayList()

    @Throws(ServletException::class)
    override fun init(filterConfig: FilterConfig) {
        val tempExcludes = filterConfig.getInitParameter("excludes")
        if (StringUtils.isNotEmpty(tempExcludes)) {
            val url = tempExcludes.split(StringUtilsExtend.SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()
            var i = 0
            while (i < url.size) {
                excludes.add(url[i])
                i++
            }
        }
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse?, chain: FilterChain) {
        val req = request as HttpServletRequest
        val resp = response as HttpServletResponse?
        if (handleExcludeURL(req, resp)) {
            chain.doFilter(request, response)
            return
        }
        val xssRequest = XssHttpServletRequestWrapper(request)
        chain.doFilter(xssRequest, response)
    }

    private fun handleExcludeURL(request: HttpServletRequest, response: HttpServletResponse?): Boolean {
        val url = request.servletPath
        val method = request.method
        // GET DELETE 不过滤
        return if (method == null || HttpMethod.GET.matches(method) || HttpMethod.DELETE.matches(method)) {
            true
        } else matches(url, excludes)
    }

    override fun destroy() {}
}
