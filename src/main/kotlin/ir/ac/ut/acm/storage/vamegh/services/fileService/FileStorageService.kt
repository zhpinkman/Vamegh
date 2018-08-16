package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.controllers.file.models.DeleteRequest
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.web.multipart.MultipartFile

interface FileStorageService{
    fun store (file:MultipartFile , user: User, path: String)
    fun mkDir(name:String , parentPath: String )
    fun createFileEntityOnDb( name: String , size: Long , parentPath: String, isParentUnderBucket: Boolean , isDir: Boolean , type: String)
    fun getFilesList(path: String , user: User): List<FileEntity>
    fun renameFile(renameRequest: RenameRequest, user: User)
    fun deleteFile(deleteRequest: DeleteRequest, user: User)
    fun copyFile(path: String, user: User,newPath : String)
    fun moveFile(path: String, user: User,newPath : String)
}