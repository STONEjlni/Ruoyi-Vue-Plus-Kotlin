package com.blank.common.web.utils

import cn.hutool.captcha.generator.CodeGenerator
import cn.hutool.core.math.Calculator
import cn.hutool.core.util.CharUtil
import cn.hutool.core.util.RandomUtil
import org.apache.commons.lang3.StringUtils
import java.io.Serial
import kotlin.math.max
import kotlin.math.min

/**
 * 无符号计算生成器
 */
class UnsignedMathGenerator : CodeGenerator {
    companion object {
        @Serial
        private const val serialVersionUID = -5514819971774091076L

        private const val OPERATORS = "+-*"
    }

    /**
     * 参与计算数字最大长度
     */
    private var numberLength = 0

    /**
     * 构造
     */
    constructor() : this(2)

    /**
     * 构造
     *
     * @param numberLength 参与计算最大数字位数
     */
    constructor(numberLength: Int) {
        this.numberLength = numberLength
    }

    override fun generate(): String {
        val limit = getLimit()
        val a = RandomUtil.randomInt(limit)
        val b = RandomUtil.randomInt(limit)
        var max = max(a, b).toString()
        var min = min(a, b).toString()
        max = StringUtils.rightPad(max, numberLength, CharUtil.SPACE)
        min = StringUtils.rightPad(min, numberLength, CharUtil.SPACE)
        return max + RandomUtil.randomChar(OPERATORS) + min + '='
    }

    override fun verify(code: String?, userInputCode: String): Boolean {
        val result: Int
        result = try {
            userInputCode.toInt()
        } catch (e: NumberFormatException) {
            // 用户输入非数字
            return false
        }
        val calculateResult = Calculator.conversion(code).toInt()
        return result == calculateResult
    }

    /**
     * 获取验证码长度
     *
     * @return 验证码长度
     */
    fun getLength(): Int {
        return numberLength * 2 + 2
    }

    /**
     * 根据长度获取参与计算数字最大值
     *
     * @return 最大值
     */
    private fun getLimit(): Int {
        return "1${StringUtils.repeat('0', numberLength)}".toInt()
    }
}
