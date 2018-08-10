package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.controllers.file.models.DeleteRequest
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.exceptions.EntityNotFound
import ir.ac.ut.acm.storage.vamegh.exceptions.NotUniqueException
import ir.ac.ut.acm.storage.vamegh.exceptions.UnableToCreateDirectory
import ir.ac.ut.acm.storage.vamegh.exceptions.UnexcpectedNullException
import ir.ac.ut.acm.storage.vamegh.repositories.FileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.util.*


@Service
class FileStorageServiceImpl : FileStorageService {


    val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String

    @Autowired
    lateinit var fileRepository: FileRepository

    override fun renameFile(renameRequest: RenameRequest, user: User) {
        try {
            val parentPath: String
            if(renameRequest.parentPath != "/")
                parentPath =  "/" +  user.bucketName + renameRequest.parentPath
            else
                parentPath =   "/" + user.bucketName
            val file = File("$rootLocation$parentPath/${renameRequest.oldName}")
            file.renameTo(File("$rootLocation$parentPath/${renameRequest.newName}"))
            val fileEntity = fileRepository.findByPath("$parentPath/${renameRequest.oldName}")
                    ?: throw EntityNotFound("file not found")

            fileEntity.name = renameRequest.newName
            fileRepository.save(fileEntity)
        }
        catch(e: Exception){
            logger.error("Error in Renaming File: ${e.message}")
            throw e
        }

    }
    override fun deleteFile(deleteRequest: DeleteRequest , user: User) {
        val entityPath = "${user.bucketName}${deleteRequest.path}"
        val fileEntity = fileRepository.findByPath(entityPath)
                ?: throw EntityNotFound("file with path $entityPath not found")
        fileRepository.delete(fileEntity)
//        fileRepository.deleteById(fileEntity.id ?: throw UnexcpectedNullException("entity id is null"))

        val diskPath = "$rootLocation/${user.bucketName}${deleteRequest.path}"
        val file = File(diskPath)
        file.delete()

    }

    override fun createFileEntityOnDb(name: String, size: Long, parentPath: String, isParentUnderBucket: Boolean, isDir: Boolean, type: String) {
        try {
            val now = Date()
            val parentId: String?
            if(isParentUnderBucket)
                parentId = fileRepository.findByPath(parentPath)?.id ?: throw UnexcpectedNullException("id is null")
            else
                parentId = parentPath
            val completePath = "$parentPath/$name"
            this.fileRepository.save(FileEntity(name = name , size=size ,parentId = parentId ,creationDate = now , isDir=isDir , path = completePath , type = type ))
        }
        catch (e: DuplicateKeyException) {
            throw NotUniqueException("Chosen Email or Bucket name is not unique")
        }
    }

    override fun store(file: MultipartFile  , user: User, path: String){
        try{
            val parentPath : String
            if(path == "/"){
                parentPath = "/${user.bucketName}"
            }
            else
                parentPath = "/${user.bucketName}$path"
            val completePath = "$rootLocation$parentPath/${file.originalFilename}"
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
            val completeParentPath = "/${user.bucketName}"
            val parentId: String
            if(path != "/")
                parentId =  this.fileRepository.findByPath(completeParentPath + path)?.id ?: throw UnexcpectedNullException("id of file entity found null")
            else
                parentId =  this.fileRepository.findByPath(completeParentPath)?.id ?: throw UnexcpectedNullException("id of file entity found null")
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
                completeParentPath = ""
            }
            else
                completeParentPath = parentPath
            val created = File("$rootLocation$completeParentPath/$name").mkdir()
            if(!created)
                throw UnableToCreateDirectory("Directory could not be created!")
            this.createFileEntityOnDb( name = name , isDir = true , size = 0 , parentPath = completeParentPath , isParentUnderBucket = isParentUnderBucket , type = "dir" )

        }
        catch(e: Exception){
            logger.error("Error in Creating Directory: ${e.message}")
            throw e
        }

    }


}