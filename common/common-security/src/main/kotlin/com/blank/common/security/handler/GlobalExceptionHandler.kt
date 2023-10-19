package com.blank.common.security.handler

import cn.dev33.satoken.exception.NotLoginException
import cn.dev33.satoken.exception.NotPermissionException
import cn.dev33.satoken.exception.NotRoleException
import cn.hutool.core.util.ObjectUtil
import cn.hutool.http.HttpStatus
import com.blank.common.core.annotation.Slf4j
import com.blank.common.core.annotation.Slf4j.Companion.log
import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.fail
import com.blank.common.core.exception.DemoModeException
import com.blank.common.core.exception.ServiceException
import com.blank.common.core.exception.base.BaseException
import com.blank.common.core.utils.StreamUtils.join
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolation
import jakarta.validation.ConstraintViolationException
import org.springframework.validation.BindException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.util.function.Function

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {
    /**
     * 权限码异常
     */
    @ExceptionHandler(NotPermissionException::class)
    fun handleNotPermissionException(e: NotPermissionException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求地址'$requestURI',权限码校验失败'${e.message}'" }
        return fail(HttpStatus.HTTP_FORBIDDEN, "没有访问权限，请联系管理员授权")
    }

    /**
     * 角色权限异常
     */
    @ExceptionHandler(NotRoleException::class)
    fun handleNotRoleException(e: NotRoleException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求地址'$requestURI',角色权限校验失败'${e.message}'" }
        return fail(HttpStatus.HTTP_FORBIDDEN, "没有访问权限，请联系管理员授权")
    }

    /**
     * 认证失败
     */
    @ExceptionHandler(NotLoginException::class)
    fun handleNotLoginException(e: NotLoginException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求地址'$requestURI',认证失败'${e.message}',无法访问系统资源" }
        return fail(HttpStatus.HTTP_UNAUTHORIZED, "认证失败，无法访问系统资源")
    }

    /**
     * 请求方式不支持
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException::class)
    fun handleHttpRequestMethodNotSupported(
        e: HttpRequestMethodNotSupportedException,
        request: HttpServletRequest
    ): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求地址'$requestURI',不支持'${e.method}'请求" }
        return fail(msg = e.message)
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException::class)
    fun handleServiceException(e: ServiceException, request: HttpServletRequest?): R<Void> {
        log.error { e.message }
        val code = e.code
        return if (ObjectUtil.isNotNull(code)) fail(code = code, msg = e.message) else fail(msg = e.message)
    }

    /**
     * 业务异常
     */
    @ExceptionHandler(BaseException::class)
    fun handleBaseException(e: BaseException, request: HttpServletRequest?): R<Void> {
        log.error { e.message }
        return fail(msg = e.message)
    }

    /**
     * 请求路径中缺少必需的路径变量
     */
    @ExceptionHandler(MissingPathVariableException::class)
    fun handleMissingPathVariableException(e: MissingPathVariableException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求路径中缺少必需的路径变量'$requestURI',发生系统异常." }
        return fail(msg = "请求路径中缺少必需的路径变量[${e.variableName}]")
    }

    /**
     * 请求参数类型不匹配
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleMethodArgumentTypeMismatchException(
        e: MethodArgumentTypeMismatchException,
        request: HttpServletRequest
    ): R<Void> {
        val requestURI = request.requestURI
        log.error { "请求参数类型不匹配'$requestURI',发生系统异常." }
        return fail(
            msg = "请求参数类型不匹配，参数[${e.name}]要求类型为：'${e.requiredType?.getName()}'，但输入值为：'${e.value}'"
        )
    }

    /**
     * 拦截未知的运行时异常
     */
    @ExceptionHandler(RuntimeException::class)
    fun handleRuntimeException(e: RuntimeException, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error(e) { "请求地址'$requestURI',发生未知异常." }
        return fail(msg = e.message)
    }

    /**
     * 系统异常
     */
    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception, request: HttpServletRequest): R<Void> {
        val requestURI = request.requestURI
        log.error(e) { "请求地址'$requestURI',发生系统异常." }
        return fail(msg = e.message)
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException::class)
    fun handleBindException(e: BindException): R<Void> {
        log.error { e.message }
        val message = join(
            e.allErrors,
            Function { it.defaultMessage.let { "" } }, ", "
        )
        return fail(msg = message)
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolationException(e: ConstraintViolationException): R<Void> {
        log.error { e.message }
        val message = join(
            e.constraintViolations,
            { obj: ConstraintViolation<*> -> obj.message }, ", "
        )
        return fail(msg = message)
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): R<Void> {
        log.error { e.message }
        val message = e.bindingResult.fieldError!!.defaultMessage
        return fail(msg = message)
    }

    /**
     * 演示模式异常
     */
    @ExceptionHandler(DemoModeException::class)
    fun handleDemoModeException(e: DemoModeException?): R<Void> {
        return fail(msg = "演示模式，不允许操作")
    }
}
