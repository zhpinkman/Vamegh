package ir.ac.ut.acm.storage.vamegh.Services

import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile

@Service
class FileStorageServiceImpl : FileStorageService {
    override fun store(file: MultipartFile, user: User, path: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}