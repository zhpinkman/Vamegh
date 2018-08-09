package ir.ac.ut.acm.storage.vamegh.services.fileService
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
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


@Service
class FileStorageServiceImpl : FileStorageService {


    val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String

    @Autowired
    lateinit var fileRepository: FileRepository

    override fun renameFile(renameRequest: RenameRequest , user: User) {
        try {
            val parentPath: String
            if(renameRequest.parentPath != "/")
                parentPath = rootLocation + "/" +  user.bucketName + renameRequest.parentPath
            else
                parentPath = rootLocation +  "/" + user.bucketName
            val file = File("$parentPath/${renameRequest.oldName}")
            file.renameTo(File("$parentPath/${renameRequest.newName}"))
            val fileEntity = fileRepository.findByPath("$parentPath/${renameRequest.oldName}")
            fileEntity.name = renameRequest.newName
            fileRepository.save(fileEntity)
        }
        catch(e: Exception){
            logger.error("Error in Renaming File: ${e.message}")
            throw e
        }

    }

    override fun createFileEntityOnDb(name: String, size: Long, parentPath: String, isParentUnderBucket: Boolean, isDir: Boolean, type: String) {
        try {
            val now = LocalDateTime.now()
            val parentId: String?
            if(isParentUnderBucket)
                parentId = fileRepository.findByPath(parentPath).id
            else
                parentId = parentPath
            val completePath = "$parentPath/$name"
            this.fileRepository.save(FileEntity(name = name , size=size ,parentId = parentId ,creationDate = now , isDir=isDir , path = completePath , type = type ))
        }
        catch(e: Exception){
            logger.error("Error in Creating File Entity On Data Base: ${e.message}")
            throw e
        }
    }

    override fun store(file: MultipartFile, user: User, path: String) {
        try{
            val parentPath : String
            if(path == "/"){
                parentPath = "$rootLocation/${user.bucketName}"
            }
            else
                parentPath = "$rootLocation/${user.bucketName}$path"
            val completePath = "$parentPath/${file.originalFilename}"
            val newFile = File(completePath)
            newFile.createNewFile()
            file.transferTo(newFile)
            this.createFileEntityOnDb( name = file.originalFilename!! ,isDir = false ,  size = file.size , parentPath = parentPath , isParentUnderBucket = true , type = file.contentType!! )

        }
        catch(e: Exception){
            logger.error("Error in saving file: ${e.message}")
            throw e
        }
    }

    override fun getFilesList(path: String, user: User): List<FileEntity> {
        try {
            val completeParentPath = "$rootLocation/${user.bucketName}"
            val parentId: String?
            if(path != "/")
                parentId = this.fileRepository.findByPath(completeParentPath + path).id
            else
                parentId =  this.fileRepository.findByPath(completeParentPath).id
            println(this.fileRepository.findAllByParentId(parentId))
            return this.fileRepository.findAllByParentId(parentId)
        } catch(e: Exception){
            logger.error("Error in  getting Files list: ${e.message}")
            throw e
        }
    }

    override fun mkDir(name: String, parentPath: String) {
        try{
            val completeParentPath  : String
            var isParentUnderBucket = true
            if(parentPath == "/"){
                isParentUnderBucket = false
                completeParentPath = rootLocation
            }
            else
                completeParentPath = "$rootLocation$parentPath"
            File("$completeParentPath/$name").mkdir()
            this.createFileEntityOnDb( name = name , isDir = true , size = 0 , parentPath = completeParentPath , isParentUnderBucket = isParentUnderBucket , type = "Directory" )
        }
        catch(e: Exception){
            logger.error("Error in Creating Directory: ${e.message}")
            throw e
        }

    }

}