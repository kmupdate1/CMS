package jp.wataju.mail

import io.ktor.server.config.*
import java.io.UnsupportedEncodingException
import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

fun assembleEmail(
    destination: String,
    userName: String,
    emailConfig: ApplicationConfig
) {
    val settingConfig = emailConfig.config("setting")

    val yahoo = settingConfig.config("yahoo")
    val contentMessageFromYahoo = assemble(yahoo) {
        val host = yahoo.property("host").getString()
        val port = yahoo.property("port").getString()
        val from = yahoo.property("address").getString()
        val loginId = yahoo.property("loginId").getString()
        val password = yahoo.property("password").getString()

        val content = emailConfig.config("content")
        val subject = content.property("subject").getString()
        val senderName = content.property("senderName").getString()

        // smtp認証
        val setting = mapOf(
            "mail.smtp.host" to host,
            "mail.smtp.port" to port,
            //"mail.transport.protocol"       to "smtp",
            "mail.smtp.auth" to "true",
            "mail.smtp.auth.login.disable" to "true",
            "mail.smtp.starttls.enable" to "true",
            //"mail.smtp.starttls.required"   to "true",
            //"mail.smtp.ssl.enable"          to "false",
            //"mail.smtp.socketFactory.class" to "javax.net.ssl.SSLSocketFactory",
        )

        val session = Session.getInstance(
            Properties().apply {
                setting.forEach {
                    this[it.key] = it.value
                }
            },
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(loginId, password)
                }
            }
        )

        // 配信コンテンツ・送信先・送信元の設定
        val contentMessage = MimeMessage(session)
        val mailContent = """
            <html>
                <body>
                    <h1>こんにちは、$userName さん</h1>
                    <p>
                        $senderName より自動送信しております。<br>
                        返信はできませんので、ご了承ください。
                    </p>
                </body>
            </html>
        """.trimIndent()

        contentMessage.apply {
            this.setFrom(InternetAddress(from, senderName, "UTF-8"))
            this.addRecipient(Message.RecipientType.TO, InternetAddress(destination, userName, "UTF-8"))
            this.setSubject(subject, "UTF-8")
            this.setHeader("Content-Type", "text/html; charset=UTF-8")
            this.setContent(mailContent, "text/html; charset=UTF-8")
            this.sentDate = Date()
        }

        try {

            Transport.send(contentMessage)

        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: AuthenticationFailedException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }

    val google = settingConfig.config("google")
    assemble(google) {
        val host = google.property("host").getString()
        val port = google.property("port").getString()
        val from = google.property("address").getString()
        val loginId = google.property("loginId").getString()
        val password = google.property("password").getString()

        val content = emailConfig.config("content")
        val subject = content.property("subject").getString()
        val senderName = content.property("senderName").getString()

        // smtp認証
        val setting = mapOf(
            "mail.smtp.host" to host,
            "mail.smtp.port" to port,
            //"mail.transport.protocol"       to "smtp",
            "mail.smtp.auth" to "true",
            "mail.smtp.auth.login.disable" to "true",
            "mail.smtp.starttls.enable" to "true",
            //"mail.smtp.starttls.required"   to "true",
            //"mail.smtp.ssl.enable"          to "false",
            //"mail.smtp.socketFactory.class" to "javax.net.ssl.SSLSocketFactory",
        )

        val session = Session.getInstance(
            Properties().apply {
                setting.forEach {
                    this[it.key] = it.value
                }
            },
            object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(loginId, password)
                }
            }
        )

        // 配信コンテンツ・送信先・送信元の設定
        val contentMessage = MimeMessage(session)
        val mailContent = """
            <html>
                <body>
                    <h1>こんにちは、$userName さん</h1>
                    <p>
                        $senderName より自動送信しております。<br>
                        返信はできませんので、ご了承ください。
                    </p>
                </body>
            </html>
        """.trimIndent()

        contentMessage.apply {
            this.setFrom(InternetAddress(from, senderName, "UTF-8"))
            this.addRecipient(Message.RecipientType.TO, InternetAddress(destination, userName, "UTF-8"))
            this.setSubject(subject, "UTF-8")
            this.setHeader("Content-Type", "text/html; charset=UTF-8")
            this.setContent(mailContent, "text/html; charset=UTF-8")
            this.sentDate = Date()
        }

        try {

            Transport.send(contentMessage)

        } catch (e: NoSuchProviderException) {
            e.printStackTrace()
        } catch (e: AuthenticationFailedException) {
            e.printStackTrace()
        } catch (e: MessagingException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
    }
}

fun assemble(
    provider: ApplicationConfig,
    block: () -> Unit
) {
    if (provider.property("enabled").getString() == "true" ) {
        block()
    }
}

