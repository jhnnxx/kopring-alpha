package org.jhnnx.jhnnxalpha.service

import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service

@Service
class EmailService(
    private val mailSender: JavaMailSender,
) {
    fun sendEmail(
        to: String,
        subject: String,
        content: String,
    ) {
        val message = mailSender.createMimeMessage()
        val helper = MimeMessageHelper(message, true, "UTF-8")

        helper.setTo(to)
        helper.setSubject(subject)
        helper.setText(content, true)

        mailSender.send(message)
    }
}
