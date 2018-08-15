package ir.ac.ut.acm.storage.vamegh.services.mailService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailContentBuilderServiceImpl : MailContentBuilderService {

    @Autowired
    private lateinit var templateEngine: TemplateEngine

    override fun build(link: String): String {

        val context = Context()
        context.setVariable("link", link)
        return templateEngine.process("mailTemplate", context)    }
}