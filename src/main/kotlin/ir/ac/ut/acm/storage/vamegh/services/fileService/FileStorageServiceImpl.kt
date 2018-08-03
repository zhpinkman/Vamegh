package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.entities.User
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File

@Service
class FileStorageServiceImpl : FileStorageService {

    val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String

    override fun store(file: MultipartFile  , user: User, path: String){
        try{
            val newFile = File("$rootLocation/${user.bucketName}$path/${file.originalFilename}")
            newFile.createNewFile()
            file.transferTo(newFile)
        }
        catch(e: Exception){
            logger.error("Error in saving file: ${e.message}")
            throw e;
        }
    }

}