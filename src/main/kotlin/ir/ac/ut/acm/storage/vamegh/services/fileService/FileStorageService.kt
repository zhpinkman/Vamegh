package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.controllers.file.models.DeleteRequest
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.FileInfo
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.CopyRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.MoveRequest
import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.web.multipart.MultipartFile

interface FileStorageService{
    fun store (file:MultipartFile , user: User, path: String)
    fun mkDir(name:String , parentPath: String, user: User )
    fun createFileEntityOnDb( name: String , size: Long , parentPath: String, isParentUnderBucket: Boolean , isDir: Boolean , type: String, userId: String?)
    fun getFilesList(path: String , user: User): List<FileInfo>
    fun renameFile(renameRequest: RenameRequest, user: User)
    fun deleteFile(deleteRequest: DeleteRequest, user: User)
    fun copyFile(copyRequest: CopyRequest, user: User)
    fun moveFile(moveRequest: MoveRequest, user: User)
    fun existsAndIsAllowed(path:String , user: User):Boolean
    fun toggleFileVisiblity(path: String , user: User)
    fun search(text: String, user: User): List<FileInfo>

}