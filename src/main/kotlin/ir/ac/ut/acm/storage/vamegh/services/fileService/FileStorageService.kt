package ir.ac.ut.acm.storage.vamegh.services.fileService

import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.web.multipart.MultipartFile

interface FileStorageService{
    fun store (file:MultipartFile , user: User, path: String)
    fun mkDir(path: String)
    fun creatFileEntityOnDb(path: String, isDir: Boolean)
    fun getFilesList(path: String): List<FileEntity>
    fun renameFile(parentPath: String, oldFileName: String, newFileName: String)
}