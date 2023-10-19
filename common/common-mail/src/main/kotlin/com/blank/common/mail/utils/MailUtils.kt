package com.blank.common.mail.utils

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.io.IoUtil
import cn.hutool.core.map.MapUtil
import cn.hutool.core.util.CharUtil
import cn.hutool.core.util.StrUtil
import cn.hutool.extra.spring.SpringUtil
import com.blank.common.core.utils.StringUtilsExtend.blankToDefault
import com.blank.common.mail.utils.Mail.Companion.create
import jakarta.mail.Authenticator
import jakarta.mail.Session
import java.io.File
import java.io.InputStream

/**
 * 邮件工具类
 */
object MailUtils {
    private val ACCOUNT = SpringUtil.getBean(MailAccount::class.java)

    /**
     * 获取邮件发送实例
     */
    fun getMailAccount(): MailAccount {
        return ACCOUNT
    }

    /**
     * 获取邮件发送实例 (自定义发送人以及授权码)
     *
     * @param user 发送人
     * @param pass 授权码
     */
    fun getMailAccount(from: String, user: String, pass: String): MailAccount {
        ACCOUNT.from = blankToDefault(from, ACCOUNT.from!!)
        ACCOUNT.user = blankToDefault(user, ACCOUNT.user!!)
        ACCOUNT.pass = blankToDefault(pass, ACCOUNT.pass!!)
        return ACCOUNT
    }

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给单个或多个收件人<br></br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun sendText(to: String,
                 subject: String,
                 content: String,
                 vararg files: File): String {
        return send(to, subject, content, false, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br></br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun sendHtml(to: String,
                 subject: String,
                 content: String,
                 vararg files: File): String {
        return send(to, subject, content, true, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br></br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     */
    fun send(to: String,
             subject: String,
             content: String,
             isHtml: Boolean,
             vararg files: File): String {
        return send(splitAddress(to)!!, subject, content, isHtml, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br></br>
     * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to      收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param cc      抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param bcc     密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     * @since 4.0.3
     */
    fun send(
        to: String,
        cc: String,
        bcc: String,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(splitAddress(to)!!,
            splitAddress(cc),
            splitAddress(bcc),
            subject, content, isHtml,
            files = files)
    }

    /**
     * 使用配置文件中设置的账户发送文本邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     */
    fun sendText(tos: Collection<String>,
                 subject: String,
                 content: String,
                 vararg files: File): String {
        return send(tos, subject, content, false, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param files   附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun sendHtml(tos: Collection<String>,
                 subject: String,
                 content: String,
                 vararg files: File): String {
        return send(tos, subject, content, true, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     */
    fun send(
        tos: Collection<String>,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(tos, null, null, subject, content, isHtml, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos     收件人列表
     * @param ccs     抄送人列表，可以为null或空
     * @param bccs    密送人列表，可以为null或空
     * @param subject 标题
     * @param content 正文
     * @param isHtml  是否为HTML
     * @param files   附件列表
     * @return message-id
     * @since 4.0.3
     */
    fun send(
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(
            getMailAccount(), true,
            tos, ccs, bccs,
            subject, content, null, isHtml, files = files)
    }

    // ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount

    // ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount
    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件认证对象
     * @param to          收件人，多个收件人逗号或者分号隔开
     * @param subject     标题
     * @param content     正文
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun send(
        mailAccount: MailAccount,
        to: String,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(mailAccount, splitAddress(to)!!, subject, content, isHtml, files = files)
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param subject     标题
     * @param content     正文
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     */
    fun send(
        mailAccount: MailAccount,
        tos: Collection<String>,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(mailAccount, tos, null, null, subject, content, isHtml, files = files)
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param ccs         抄送人列表，可以为null或空
     * @param bccs        密送人列表，可以为null或空
     * @param subject     标题
     * @param content     正文
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     * @since 4.0.3
     */
    fun send(
        mailAccount: MailAccount,
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(
            mailAccount, false, tos, ccs, bccs, subject,
            content, null, isHtml, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给单个或多个收件人<br></br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files    附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun sendHtml(
        to: String,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        vararg files: File
    ): String {
        return send(to, subject, content, imageMap, true, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br></br>
     * 多个收件人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     */
    fun send(
        to: String,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(splitAddress(to)!!, subject, content, imageMap, isHtml, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送单个或多个收件人<br></br>
     * 多个收件人、抄送人、密送人可以使用逗号“,”分隔，也可以通过分号“;”分隔
     *
     * @param to       收件人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param cc       抄送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param bcc      密送人，可以使用逗号“,”分隔，也可以通过分号“;”分隔
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     * @since 4.0.3
     */
    fun send(
        to: String,
        cc: String,
        bcc: String,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(
            splitAddress(to)!!,
            splitAddress(cc),
            splitAddress(bcc),
            subject, content, imageMap, isHtml, files = files
        )
    }

    /**
     * 使用配置文件中设置的账户发送HTML邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files    附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun sendHtml(
        tos: Collection<String>,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        vararg files: File
    ): String {
        return send(tos, subject, content, imageMap, true, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     */
    fun send(
        tos: Collection<String>,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(tos, null, null, subject, content, imageMap, isHtml, files = files)
    }

    /**
     * 使用配置文件中设置的账户发送邮件，发送给多人
     *
     * @param tos      收件人列表
     * @param ccs      抄送人列表，可以为null或空
     * @param bccs     密送人列表，可以为null或空
     * @param subject  标题
     * @param content  正文
     * @param imageMap 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml   是否为HTML
     * @param files    附件列表
     * @return message-id
     * @since 4.0.3
     */
    fun send(
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(
            getMailAccount(), true, tos, ccs, bccs,
            subject, content, imageMap, isHtml, files = files)
    }

    // ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount

    // ------------------------------------------------------------------------------------------------------------------------------- Custom MailAccount
    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件认证对象
     * @param to          收件人，多个收件人逗号或者分号隔开
     * @param subject     标题
     * @param content     正文
     * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     * @since 3.2.0
     */
    fun send(
        mailAccount: MailAccount,
        to: String,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(mailAccount, splitAddress(to)!!, subject, content, imageMap, isHtml, files = files)
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param subject     标题
     * @param content     正文
     * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     * @since 4.6.3
     */
    fun send(
        mailAccount: MailAccount,
        tos: Collection<String>,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(mailAccount, tos, null, null, subject, content, imageMap, isHtml, files = files)
    }

    /**
     * 发送邮件给多人
     *
     * @param mailAccount 邮件帐户信息
     * @param tos         收件人列表
     * @param ccs         抄送人列表，可以为null或空
     * @param bccs        密送人列表，可以为null或空
     * @param subject     标题
     * @param content     正文
     * @param imageMap    图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param isHtml      是否为HTML格式
     * @param files       附件列表
     * @return message-id
     * @since 4.6.3
     */
    fun send(
        mailAccount: MailAccount,
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        return send(mailAccount,
            false,
            tos, ccs, bccs, subject, content, imageMap, isHtml, files = files)
    }

    /**
     * 根据配置文件，获取邮件客户端会话
     *
     * @param mailAccount 邮件账户配置
     * @param isSingleton 是否单例（全局共享会话）
     * @return [Session]
     * @since 5.5.7
     */
    fun getSession(mailAccount: MailAccount, isSingleton: Boolean): Session {
        var authenticator: Authenticator? = null
        if (mailAccount.auth!!) {
            authenticator = UserPassAuthenticator(mailAccount.user!!, mailAccount.pass!!)
        }
        return if (isSingleton) Session.getDefaultInstance(mailAccount.getSmtpProps(), authenticator) //
        else Session.getInstance(mailAccount.getSmtpProps(), authenticator)
    }

    // ------------------------------------------------------------------------------------------------------------------------ Private method start

    // ------------------------------------------------------------------------------------------------------------------------ Private method start
    /**
     * 发送邮件给多人
     *
     * @param mailAccount      邮件帐户信息
     * @param useGlobalSession 是否全局共享Session
     * @param tos              收件人列表
     * @param ccs              抄送人列表，可以为null或空
     * @param bccs             密送人列表，可以为null或空
     * @param subject          标题
     * @param content          正文
     * @param imageMap         图片与占位符，占位符格式为cid:${cid}
     * @param isHtml           是否为HTML格式
     * @param files            附件列表
     * @return message-id
     * @since 4.6.3
     */
    private fun send(
        mailAccount: MailAccount,
        useGlobalSession: Boolean,
        tos: Collection<String>,
        ccs: Collection<String>?,
        bccs: Collection<String>?,
        subject: String,
        content: String,
        imageMap: Map<String, InputStream>?,
        isHtml: Boolean,
        vararg files: File
    ): String {
        val mail = create(mailAccount).apply {
            this.useGlobalSession = useGlobalSession
        }

        // 可选抄送人
        if (CollUtil.isNotEmpty(ccs)) {
            mail.ccs = ccs!!.toTypedArray<String>()
        }
        // 可选密送人
        if (CollUtil.isNotEmpty(bccs)) {
            mail.bccs = bccs!!.toTypedArray<String>()
        }
        mail.tos = tos.toTypedArray<String>()
        mail.title = subject
        mail.content = content
        mail.isHtml = isHtml
        mail.setFiles(*files)

        // 图片
        if (MapUtil.isNotEmpty(imageMap)) {
            for ((key, value) in imageMap!!) {
                mail.addImage(key, value)
                // 关闭流
                IoUtil.close(value)
            }
        }
        return mail.send()
    }

    /**
     * 将多个联系人转为列表，分隔符为逗号或者分号
     *
     * @param addresses 多个联系人，如果为空返回null
     * @return 联系人列表
     */
    private fun splitAddress(addresses: String): List<String>? {
        if (StrUtil.isBlank(addresses)) {
            return null
        }
        val result: List<String>
        result = if (StrUtil.contains(addresses, CharUtil.COMMA)) {
            StrUtil.splitTrim(addresses, CharUtil.COMMA)
        } else if (StrUtil.contains(addresses, ';')) {
            StrUtil.splitTrim(addresses, ';')
        } else {
            CollUtil.newArrayList(addresses)
        }
        return result
    }
    // ------------------------------------------------------------------------------------------------------------------------ Private method end

}
