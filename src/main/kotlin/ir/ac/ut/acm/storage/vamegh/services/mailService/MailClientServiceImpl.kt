package ir.ac.ut.acm.storage.vamegh.services.mailService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.mail.MailException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator



@Service
class MailClientServiceImpl : MailClientService {

    @Autowired
    private lateinit var mailSender: JavaMailSender

    @Autowired
    private lateinit var mailContentBuilderService: MailContentBuilderService

    override fun prepareAndSend(recipient: String, link: String) {
        val messagePreparator = MimeMessagePreparator { mimeMessage ->
            run {
                val messageHelper = MimeMessageHelper(mimeMessage)
                messageHelper.setFrom("utsoccloud@gmail.com")
                messageHelper.setTo(recipient)
                messageHelper.setSubject("Verify Your Email")
                val content = mailContentBuilderService.build(link)
                messageHelper.setText(content, true)
            }
        }
        try {
            mailSender.send(messagePreparator)
        } catch (e: MailException) {
            // runtime exception; compiler will not force you to handle it
        }

    }
}