package com.blank.common.encrypt.filter

import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.encrypt.properties.ApiDecryptProperties
import jakarta.servlet.*
import jakarta.servlet.http.HttpServletRequest
import org.apache.commons.lang3.StringUtils
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import java.io.IOException

/**
 * Crypto 过滤器
 */
class CryptoFilter(
    private val properties: ApiDecryptProperties
) : Filter {
    @Throws(IOException::class, ServletException::class)
    override fun doFilter(request: ServletRequest, response: ServletResponse?, chain: FilterChain) {
        var requestWrapper: ServletRequest? = null
        val servletRequest = request as HttpServletRequest
        // 是否为 json 请求
        if (StringUtils.startsWithIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE)) {
            // 是否为 put 或者 post 请求
            if (HttpMethod.PUT.matches(servletRequest.method) || HttpMethod.POST.matches(servletRequest.method)) {
                // 是否存在加密标头
                val headerValue = servletRequest.getHeader(properties.headerFlag)
                if (StrUtil.isNotBlank(headerValue)) {
                    requestWrapper = DecryptRequestBodyWrapper(
                        servletRequest,
                        properties.publicKey,
                        properties.privateKey,
                        properties.headerFlag
                    )
                }
            }
        }
        chain.doFilter(ObjectUtil.defaultIfNull(requestWrapper, request), response)
    }

    override fun destroy() {}
}
