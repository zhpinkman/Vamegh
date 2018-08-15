package ir.ac.ut.acm.storage.vamegh.services.mailService

interface MailClientService {
    fun prepareAndSend(recipient: String , link: String)
}