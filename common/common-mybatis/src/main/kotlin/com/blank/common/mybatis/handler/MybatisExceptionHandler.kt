package com.blank.common.mybatis.handler

import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import jakarta.servlet.http.HttpServletRequest
import org.mybatis.spring.MyBatisSystemException
import org.springframework.dao.DuplicateKeyException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * Mybatis异常处理器
 */
@Slf4j
@RestControllerAdvice
class MybatisExceptionHandler {
    /**
     * 主键或UNIQUE索引，数据重复异常
     */
    @ExceptionHandler(DuplicateKeyException::class)
    fun handleDuplicateKeyException(e: DuplicateKeyException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求地址'$requestURI',数据库中已存在记录'${e.message}'" }
        return fail("数据库中已存在该记录，请联系管理员确认")
    }

    /**
     * Mybatis系统异常 通用处理
     */
    @ExceptionHandler(MyBatisSystemException::class)
    fun handleCannotFindDataSourceException(e: MyBatisSystemException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        val message = e.message
        if (message!!.contains("CannotFindDataSourceException")) {
            log.error { "请求地址'$requestURI', 未找到数据源" }
            return fail("未找到数据源，请联系管理员确认")
        }
        log.error(e) { "请求地址'$requestURI', Mybatis系统异常" }
        return fail(message)
    }
}
