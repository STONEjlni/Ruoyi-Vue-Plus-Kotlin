package com.blank.common.mail.utils

import cn.hutool.core.io.IORuntimeException

/**
 * 全局邮件帐户，依赖于邮件配置文件{@link MailAccount#MAIL_SETTING_PATHS}
 */
enum class GlobalMailAccount {
    INSTANCE;

    private var mailAccount: MailAccount? = null

    init {
        /**
         * 构造
         */
        mailAccount = createDefaultAccount()
    }

    /**
     * 获得邮件帐户
     *
     * @return 邮件帐户
     */
    fun getAccount(): MailAccount? {
        return mailAccount
    }

    /**
     * 创建默认帐户
     *
     * @return MailAccount
     */
    private fun createDefaultAccount(): MailAccount? {
        for (mailSettingPath in MailAccount.MAIL_SETTING_PATHS) {
            try {
                return MailAccount(mailSettingPath)
            } catch (ignore: IORuntimeException) {
                //ignore
            }
        }
        return null
    }
}
