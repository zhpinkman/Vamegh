package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.controllers.file.models.DeleteRequest
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.FileInfo
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.CopyRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.MoveRequest
import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.exceptions.*
import ir.ac.ut.acm.storage.vamegh.repositories.FileRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*


@Service
class FileStorageServiceImpl : FileStorageService {


    val logger = LoggerFactory.getLogger(this.javaClass)

    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String

    @Value("\${utCloud.baseDownloadUrl}")
    lateinit var baseDownloadUrl: String

    @Autowired
    lateinit var fileRepository: FileRepository

    override fun renameFile(renameRequest: RenameRequest, user: User) {
        try {
            val parentPath: String
            if (renameRequest.parentPath != "/")
                parentPath = "/" + user.bucketName + renameRequest.parentPath
            else
                parentPath = "/" + user.bucketName
            val file = File("$rootLocation$parentPath/${renameRequest.oldName}")
            file.renameTo(File("$rootLocation$parentPath/${renameRequest.newName}"))
            val fileEntity = fileRepository.findByPath("$parentPath/${renameRequest.oldName}")
                    ?: throw EntityNotFound("file not found")

            fileEntity.name = renameRequest.newName
            fileEntity.path = fileEntity.path.substringBeforeLast('/') + "/" + renameRequest.newName
            fileRepository.save(fileEntity)
        } catch (e: Exception) {
            logger.error("Error in Renaming File: ${e.message}")
            throw e
        }

    }

    override fun deleteFile(deleteRequest: DeleteRequest, user: User) {
        val entityPath = "/${user.bucketName}${deleteRequest.path}"
        val fileEntity = fileRepository.findByPath(entityPath)
                ?: throw EntityNotFound("file with path $entityPath not found")
        fileRepository.delete(fileEntity)
        val diskPath = "$rootLocation/${user.bucketName}${deleteRequest.path}"


        val file = File(diskPath)
        file.delete()
    }

    override fun createFileEntityOnDb(name: String, size: Long, parentPath: String, isParentUnderBucket: Boolean, isDir: Boolean, type: String) {
        try {
            val now = Date()
            val parentId: String?
            if (isParentUnderBucket)
                parentId = fileRepository.findByPath(parentPath)?.id ?: throw UnexcpectedNullException("id is null")
            else
                parentId = parentPath
            val completePath = "$parentPath/$name"
            this.fileRepository.save(FileEntity(name = name, size = size, parentId = parentId, creationDate = now, isDir = isDir, path = completePath, type = type))
        } catch (e: DuplicateKeyException) {
            throw NotUniqueException("Chosen Email or Bucket name is not unique")
        }

    }

    override fun store(file: MultipartFile, user: User, path: String) {
        try {
            val parentPath: String
            if (path == "/") {
                parentPath = "/${user.bucketName}"
            } else
                parentPath = "/${user.bucketName}$path"
            val completePath = "$rootLocation$parentPath/${file.originalFilename}"
            val newFile = File(completePath)
            newFile.createNewFile()
            file.transferTo(newFile)
            this.createFileEntityOnDb(name = file.originalFilename!!, isDir = false, size = file.size, parentPath = parentPath, isParentUnderBucket = true, type = file.contentType!!)
        } catch (e: Exception) {
            logger.error("Error in saving file: ${e.message}")
            throw e
        }
    }

    override fun getFilesList(path: String, user: User): List<FileInfo> {
        try {
            val completeParentPath = "/${user.bucketName}"
            val parentId: String
            if (path != "/")
                parentId = this.fileRepository.findByPath(completeParentPath + path)?.id ?: throw UnexcpectedNullException("id of file entity found null")
            else
                parentId = this.fileRepository.findByPath(completeParentPath)?.id ?: throw UnexcpectedNullException("id of file entity found null")
            return this.fileRepository.findAllByParentId(parentId).map { FileInfo(it, baseDownloadUrl + it.path) }
        } catch (e: Exception) {
            logger.error("Error in  getting Files list: ${e.message}")
            throw e
        }
    }

    override fun mkDir(name: String, parentPath: String) {
        try {
            val completeParentPath: String
            var isParentUnderBucket = true
            if (parentPath == "/") {
                isParentUnderBucket = false
                completeParentPath = ""
            } else
                completeParentPath = parentPath
            if (this.fileRepository.findByPath("$completeParentPath/$name") != null)
                throw DirectoryWithSameNameExists("Directory with same path and name already exists!")
            val created = File("$rootLocation$completeParentPath/$name").mkdir()
            if (!created)
                throw UnableToCreateDirectory("Directory could not be created!")
            this.createFileEntityOnDb(name = name, isDir = true, size = 0, parentPath = completeParentPath, isParentUnderBucket = isParentUnderBucket, type = "dir")

        } catch (e: Exception) {
            logger.error("Error in Creating Directory: ${e.message}")
            throw e
        }

    }

    override fun copyFile(copyRequest: CopyRequest, user: User) {
        val bucketPath: String
        if (copyRequest.oldPath == "/") {
            bucketPath = "/${user.bucketName}"
        } else
            bucketPath = "/${user.bucketName}${copyRequest.oldPath}"

        val newParentPath: String
        if (copyRequest.oldPath == "/") {
            newParentPath = "/${user.bucketName}"
        } else
            newParentPath = "/${user.bucketName}${copyRequest.newPath}"
        val completePath = "$rootLocation$bucketPath"

        val fileEntity = fileRepository.findByPath(bucketPath)
                ?: throw EntityNotFound("file with path $bucketPath not found")
        val completeNewPath = "$rootLocation$newParentPath/${fileEntity.name}"

        val source = Paths.get(completePath)
        val destination = Paths.get(completeNewPath)
        try {
            Files.copy(source, destination, StandardCopyOption.COPY_ATTRIBUTES)
            this.createFileEntityOnDb(name = fileEntity.name, isDir = false, size = fileEntity.size, parentPath = newParentPath, isParentUnderBucket = true, type = fileEntity.type)
        } catch (fileAlreadyExistsException: FileSystemException) {
            logger.error("unable to copy. ${fileAlreadyExistsException.message}")
        }
    }

    override fun moveFile(moveRequest: MoveRequest, user: User) {
        val bucketPath: String
        if (moveRequest.oldPath == "/") {
            bucketPath = "/${user.bucketName}"
        } else
            bucketPath = "/${user.bucketName}${moveRequest.oldPath}"

        val newParentPath: String
        if (moveRequest.oldPath == "/") {
            newParentPath = "/${user.bucketName}"
        } else
            newParentPath = "/${user.bucketName}${moveRequest.newPath}"
        val completePath = "$rootLocation$bucketPath"
        val fileEntity = fileRepository.findByPath(bucketPath)
                ?: throw EntityNotFound("file with path $bucketPath not found")

        val completeNewPath = "$rootLocation$newParentPath/${fileEntity.name}"

        val source = Paths.get(completePath)
        val destination = Paths.get(completeNewPath)
        try {
            Files.move(source, destination)
            this.createFileEntityOnDb(name = fileEntity.name, isDir = false, size = fileEntity.size, parentPath = newParentPath, isParentUnderBucket = true, type = fileEntity.type)
            fileRepository.delete(fileEntity)
        } catch (fileAlreadyExistsException: FileSystemException) {
            logger.error("unable to move. ${fileAlreadyExistsException.message}")
        }

    }

    override fun exists(path: String): Boolean {
        return fileRepository.existsByPath(path)

    }

    override fun search(text: String, user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}