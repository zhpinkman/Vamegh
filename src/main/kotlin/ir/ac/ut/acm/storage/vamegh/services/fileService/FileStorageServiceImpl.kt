package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.repositories.FileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.time.LocalDateTime
import java.util.*


@Service
class FileStorageServiceImpl : FileStorageService {


    val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String

    @Autowired
    lateinit var fileRepository: FileRepository

    override fun renameFile(parentPath: String, oldFileName: String, newFileName: String) {
        var file = File("$parentPath/$oldFileName")
        file.renameTo(File("$parentPath/$newFileName"))
        var user = fileRepository.findByPath("$parentPath/$oldFileName")
        user.name = newFileName
        fileRepository.save(user)

    }

    override fun creatFileEntityOnDb(path: String, isDir: Boolean){
        val now = Date()
        //date is okay but where should i get other informations
        this.fileRepository.insert(FileEntity(name = "",size=0,parentId = "",creationDate = now,isDir=isDir,path=path ));
    }

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

    override fun getFilesList(path: String): List<FileEntity> {
        val parentDir = this.fileRepository.findByPath(path)
        return this.fileRepository.findAllByParentId(parentDir.id!!)

    }

    override fun mkDir(path: String) {
        try{
            File(path).mkdir();
            this.creatFileEntityOnDb( path , true )
        }
        catch(e: Exception){
            logger.error("Error in Creating Directory: ${e.message}")
            throw e;
        }

    }

}