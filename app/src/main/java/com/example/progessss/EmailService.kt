package com.example.progessss

import java.util.*
import javax.mail.*
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeBodyPart
import javax.mail.internet.MimeMessage
import javax.mail.internet.MimeMultipart

class EmailService(private val server: String, private val port: Int) {

    data class Email(
        val auth: Authenticator,
        val toList: List<InternetAddress>,
        val from: InternetAddress,
        val subject: String,
        val body: String
    )

    class UserPassAuthenticator(private val username: String, private val password: String) : Authenticator() {
        override fun getPasswordAuthentication(): PasswordAuthentication {
            return PasswordAuthentication(username, password)
        }
    }

    fun send(email: Email) {
        val props = Properties()
        props["mail.smtp.auth"] = "true"
        props["mail.smtp.starttls.enable"] = "true"
        props["mail.smtp.host"] = server
        props["mail.smtp.port"] = port.toString()
        props["mail.smtp.ssl.trust"] = server

        val session = Session.getInstance(props, email.auth)

        try {
            val message = MimeMessage(session)
            message.setFrom(email.from)
            message.setRecipients(Message.RecipientType.TO, email.toList.toTypedArray())
            message.subject = email.subject

            // Create a multipart message for body (plain text)
            val multipart = MimeMultipart()
            val mimeBodyPart = MimeBodyPart()
            mimeBodyPart.setText(email.body, "utf-8")
            multipart.addBodyPart(mimeBodyPart)

            message.setContent(multipart)

            // Send message
            Transport.send(message)
        } catch (e: MessagingException) {
            throw RuntimeException(e)
        }
    }
}
