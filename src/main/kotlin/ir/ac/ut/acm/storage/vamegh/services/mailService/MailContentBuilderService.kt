package ir.ac.ut.acm.storage.vamegh.services.mailService

interface MailContentBuilderService {
    fun build(link: String) : String
}