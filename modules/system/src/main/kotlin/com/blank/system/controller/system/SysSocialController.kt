package com.blank.system.controller.system

import com.blank.common.core.domain.R
import com.blank.common.core.domain.R.Companion.ok
import com.blank.common.satoken.utils.LoginHelper.getUserId
import com.blank.common.web.core.BaseController
import com.blank.system.domain.vo.SysSocialVo
import com.blank.system.service.ISysSocialService
import jakarta.validation.constraints.NotNull
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/**
 * 社会化关系
 */
@Validated
@RestController
@RequestMapping("/system/social")
class SysSocialController(
    private val socialUserService: ISysSocialService
) : BaseController() {

    /**
     * 查询社会化关系列表
     */
    @GetMapping("/list")
    fun list(): R<MutableList<SysSocialVo>> {
        return ok(data = socialUserService.queryListByUserId(getUserId()!!))
    }

    /**
     * 获取社会化关系详细信息
     *
     * @param id 主键
     */
    @GetMapping("/{id}")
    fun getInfo(@PathVariable id: @NotNull(message = "主键不能为空") String): R<SysSocialVo> {
        return ok(data = socialUserService.queryById(id))
    }
}
