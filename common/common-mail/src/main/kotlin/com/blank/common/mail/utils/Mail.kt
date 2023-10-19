package com.blank.common.mail.utils

import cn.hutool.core.builder.Builder
import cn.hutool.core.io.FileUtil
import cn.hutool.core.io.IORuntimeException
import cn.hutool.core.io.IoUtil
import cn.hutool.core.util.ArrayUtil
import cn.hutool.core.util.ObjectUtil
import cn.hutool.core.util.StrUtil
import com.blank.common.mail.utils.InternalMailUtil.encodeText
import com.blank.common.mail.utils.InternalMailUtil.parseAddressFromStrs
import com.blank.common.mail.utils.InternalMailUtil.parseFirstAddress
import jakarta.activation.DataHandler
import jakarta.activation.DataSource
import jakarta.activation.FileDataSource
import jakarta.activation.FileTypeMap
import jakarta.mail.*
import jakarta.mail.internet.MimeBodyPart
import jakarta.mail.internet.MimeMessage
import jakarta.mail.internet.MimeMultipart
import jakarta.mail.internet.MimeUtility
import jakarta.mail.util.ByteArrayDataSource
import java.io.*
import java.nio.charset.Charset
import java.util.*

/**
 * 邮件发送客户端
 */
class Mail : Builder<MimeMessage> {
    companion object {
        @Serial
        private const val serialVersionUID = 1L

        /**
         * 创建邮件客户端
         *
         * @param mailAccount 邮件帐号
         * @return Mail
         */
        fun create(mailAccount: MailAccount?) = Mail(mailAccount)

        /**
         * 创建邮件客户端，使用全局邮件帐户
         *
         * @return Mail
         */
        fun create() = Mail()
    }

    /**
     * 邮箱帐户信息以及一些客户端配置信息
     */
    var mailAccount: MailAccount? = null

    /**
     * 收件人列表
     */
    var tos: Array<String> = arrayOf()

    /**
     * 抄送人列表（carbon copy）
     */
    var ccs: Array<String> = arrayOf()

    /**
     * 密送人列表（blind carbon copy）
     */
    var bccs: Array<String> = arrayOf()

    /**
     * 回复地址(reply-to)
     */
    var reply: Array<String> = arrayOf()

    /**
     * 标题
     */
    var title: String? = null

    /**
     * 内容
     * 正文可以是普通文本也可以是HTML（默认普通文本），可以通过调用[.setHtml] 设置是否为HTML
     */
    var content: String? = null

    /**
     * 是否为HTML
     */
    var isHtml = false

    /**
     * 正文、附件和图片的混合部分
     */
    private val multipart: Multipart = MimeMultipart()

    /**
     * 是否使用全局会话，默认为false
     */
    var useGlobalSession = false

    /**
     * debug输出位置，可以自定义debug日志
     */
    var debugOutput: PrintStream? = null

    // --------------------------------------------------------------- Constructor start

    // --------------------------------------------------------------- Constructor start
    /**
     * 构造，使用全局邮件帐户
     */
    constructor() : this(GlobalMailAccount.INSTANCE.getAccount())

    /**
     * 构造
     *
     * @param mailAccount 邮件帐户，如果为null使用默认配置文件的全局邮件配置
     */
    constructor(mailAccount: MailAccount?) {
        var mailAccount = mailAccount
        mailAccount = mailAccount ?: GlobalMailAccount.INSTANCE.getAccount()
        this.mailAccount = mailAccount!!.defaultIfEmpty()
    }
    // --------------------------------------------------------------- Constructor end

    // --------------------------------------------------------------- Getters and Setters start

    // --------------------------------------------------------------- Constructor end
    // --------------------------------------------------------------- Getters and Setters start
    /**
     * 设置收件人
     *
     * @param tos 收件人列表
     * @return this
     * @see .setTos
     */
    fun to(vararg tos: String): Mail {
        this.tos = arrayOf(*tos)
        return this
    }

    /**
     * 设置正文
     *
     * @param content 正文内容
     * @param isHtml  是否为HTML
     * @return this
     */
    fun setContent(content: String?, isHtml: Boolean): Mail {
        this.content = content
        this.isHtml = isHtml
        return this
    }

    /**
     * 设置文件类型附件，文件可以是图片文件，此时自动设置cid（正文中引用图片），默认cid为文件名
     *
     * @param files 附件文件列表
     * @return this
     */
    fun setFiles(vararg files: File): Mail {
        if (ArrayUtil.isEmpty(files)) {
            return this
        }
        val attachments = arrayOf<DataSource>()
        for (i in files.indices) {
            attachments[i] = FileDataSource(files[i])
        }
        return setAttachments(*attachments)
    }

    /**
     * 增加附件或图片，附件使用[DataSource] 形式表示，可以使用[FileDataSource]包装文件表示文件附件
     *
     * @param attachments 附件列表
     * @return this
     * @since 4.0.9
     */
    fun setAttachments(vararg attachments: DataSource): Mail {
        if (ArrayUtil.isNotEmpty(attachments)) {
            val charset = mailAccount!!.charset
            var bodyPart: MimeBodyPart
            var nameEncoded: String
            try {
                for (attachment in attachments) {
                    bodyPart = MimeBodyPart()
                    bodyPart.dataHandler = DataHandler(attachment)
                    nameEncoded = attachment.name
                    if (mailAccount!!.encodefilename) {
                        nameEncoded = encodeText(nameEncoded, charset)
                    }
                    // 普通附件文件名
                    bodyPart.fileName = nameEncoded
                    if (StrUtil.startWith(attachment.contentType, "image/")) {
                        // 图片附件，用于正文中引用图片
                        bodyPart.contentID = nameEncoded
                    }
                    multipart.addBodyPart(bodyPart)
                }
            } catch (e: MessagingException) {
                throw MailException(e)
            }
        }
        return this
    }

    /**
     * 增加图片，图片的键对应到邮件模板中的占位字符串，图片类型默认为"image/jpeg"
     *
     * @param cid         图片与占位符，占位符格式为cid:${cid}
     * @param imageStream 图片文件
     * @return this
     * @since 4.6.3
     */
    fun addImage(cid: String?, imageStream: InputStream?): Mail {
        return addImage(cid, imageStream, null)
    }

    /**
     * 增加图片，图片的键对应到邮件模板中的占位字符串
     *
     * @param cid         图片与占位符，占位符格式为cid:${cid}
     * @param imageStream 图片流，不关闭
     * @param contentType 图片类型，null赋值默认的"image/jpeg"
     * @return this
     * @since 4.6.3
     */
    fun addImage(cid: String?, imageStream: InputStream?, contentType: String?): Mail {
        val imgSource: ByteArrayDataSource
        imgSource = try {
            ByteArrayDataSource(imageStream, ObjectUtil.defaultIfNull(contentType, "image/jpeg"))
        } catch (e: IOException) {
            throw IORuntimeException(e)
        }
        imgSource.name = cid
        return setAttachments(imgSource)
    }

    /**
     * 增加图片，图片的键对应到邮件模板中的占位字符串
     *
     * @param cid       图片与占位符，占位符格式为cid:${cid}
     * @param imageFile 图片文件
     * @return this
     * @since 4.6.3
     */
    fun addImage(cid: String?, imageFile: File?): Mail {
        var `in`: InputStream? = null
        return try {
            `in` = FileUtil.getInputStream(imageFile)
            addImage(cid, `in`, FileTypeMap.getDefaultFileTypeMap().getContentType(imageFile))
        } finally {
            IoUtil.close(`in`)
        }
    }

    /**
     * 设置字符集编码
     *
     * @param charset 字符集编码
     * @return this
     * @see MailAccount.setCharset
     */
    fun setCharset(charset: Charset): Mail {
        mailAccount!!.charset = charset
        return this
    }

    // --------------------------------------------------------------- Getters and Setters end

    // --------------------------------------------------------------- Getters and Setters end
    override fun build(): MimeMessage {
        return try {
            buildMsg()
        } catch (e: MessagingException) {
            throw MailException(e)
        }
    }

    /**
     * 发送
     *
     * @return message-id
     * @throws MailException 邮件发送异常
     */
    @Throws(MailException::class)
    fun send(): String {
        return try {
            doSend()
        } catch (e: MessagingException) {
            if (e is SendFailedException) {
                // 当地址无效时，显示更加详细的无效地址信息
                val invalidAddresses = e.invalidAddresses
                val msg = StrUtil.format("Invalid Addresses: {}", ArrayUtil.toString(invalidAddresses))
                throw MailException(msg, e)
            }
            throw MailException(e)
        }
    }

    // --------------------------------------------------------------- Private method start

    // --------------------------------------------------------------- Private method start
    /**
     * 执行发送
     *
     * @return message-id
     * @throws MessagingException 发送异常
     */
    @Throws(MessagingException::class)
    private fun doSend(): String {
        val mimeMessage = buildMsg()
        Transport.send(mimeMessage)
        return mimeMessage.messageID
    }

    /**
     * 构建消息
     *
     * @return [MimeMessage]消息
     * @throws MessagingException 消息异常
     */
    @Throws(MessagingException::class)
    private fun buildMsg(): MimeMessage {
        val charset = mailAccount!!.charset
        val msg = MimeMessage(getSession())
        // 发件人
        val from = mailAccount!!.from!!
        if (StrUtil.isEmpty(from)) {
            // 用户未提供发送方，则从Session中自动获取
            msg.setFrom()
        } else {
            msg.setFrom(parseFirstAddress(from, charset))
        }
        // 标题
        msg.setSubject(title, charset.name())
        // 发送时间
        msg.sentDate = Date()
        // 内容和附件
        msg.setContent(buildContent(charset))
        // 收件人
        msg.setRecipients(
            MimeMessage.RecipientType.TO, parseAddressFromStrs(
                tos!!, charset
            )
        )
        // 抄送人
        if (ArrayUtil.isNotEmpty(ccs)) {
            msg.setRecipients(
                MimeMessage.RecipientType.CC, parseAddressFromStrs(
                    ccs, charset
                )
            )
        }
        // 密送人
        if (ArrayUtil.isNotEmpty<String>(bccs)) {
            msg.setRecipients(
                MimeMessage.RecipientType.BCC, parseAddressFromStrs(
                    bccs, charset
                )
            )
        }
        // 回复地址(reply-to)
        if (ArrayUtil.isNotEmpty<String>(reply)) {
            msg.replyTo = parseAddressFromStrs(reply, charset)
        }
        return msg
    }

    /**
     * 构建邮件信息主体
     *
     * @param charset 编码，`null`则使用[MimeUtility.getDefaultJavaCharset]
     * @return 邮件信息主体
     * @throws MessagingException 消息异常
     */
    @Throws(MessagingException::class)
    private fun buildContent(charset: Charset?): Multipart {
        val charsetStr = if (null != charset) charset.name() else MimeUtility.getDefaultJavaCharset()
        // 正文
        val body = MimeBodyPart()
        body.setContent(content, StrUtil.format("text/{}; charset={}", if (isHtml) "html" else "plain", charsetStr))
        multipart.addBodyPart(body)
        return multipart
    }

    /**
     * 获取默认邮件会话<br></br>
     * 如果为全局单例的会话，则全局只允许一个邮件帐号，否则每次发送邮件会新建一个新的会话
     *
     * @return 邮件会话 [Session]
     */
    private fun getSession(): Session {
        val session = MailUtils.getSession(mailAccount!!, useGlobalSession)
        if (null != debugOutput) {
            session.debugOut = debugOutput
        }
        return session
    }
    // --------------------------------------------------------------- Private method end

}
