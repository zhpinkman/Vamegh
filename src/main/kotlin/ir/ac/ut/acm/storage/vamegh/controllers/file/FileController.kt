package ir.ac.ut.acm.storage.vamegh.controllers.file


import ir.ac.ut.acm.storage.vamegh.controllers.file.models.DeleteRequest
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.FileList
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.CopyRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.MkdirRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.MoveRequest
import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
import jdk.nashorn.internal.runtime.regexp.joni.Config.log
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.security.Principal


@RestController
@RequestMapping("/file")
class FileController {

    @Autowired
    lateinit var fileStorage: FileStorageService

    @Autowired
    lateinit var userService: UserService

    @PostMapping ("/upload")
    @PreAuthorize("isAuthenticated()")
    fun uploadMultiFile(@RequestParam("file") file: MultipartFile,@RequestParam("path") path: String = "/" , principal: Principal){
        val user = userService.findByEmail(principal.name)
        fileStorage.store(file, user, path)
    }

    @PostMapping("/mkDir")
    @PreAuthorize("isAuthenticated()")
    fun createDirectory(@RequestBody mkdirRequest: MkdirRequest, principal: Principal){
        val bucketName = userService.findByEmail(principal.name).bucketName
        val user = userService.findByEmail(principal.name)
        fileStorage.mkDir(name = mkdirRequest.name, parentPath = "/" + bucketName + mkdirRequest.path, user = user)
    }

    @GetMapping ("/list")
    @PreAuthorize("isAuthenticated()")
    fun getFilesList(@RequestParam("path") path: String , principal: Principal): FileList {
        val user = userService.findByEmail(principal.name)
        return FileList(fileStorage.getFilesList( path , user))
    }

    @PostMapping ("/rename")
    @PreAuthorize("isAuthenticated()")
    fun rename(@RequestBody renameRequest: RenameRequest, principal: Principal) {
            val user = userService.findByEmail(principal.name)
            fileStorage.renameFile(renameRequest = renameRequest , user = user)
    }

    val logger = LoggerFactory.getLogger(this.javaClass)
    @PostMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    fun delete(@RequestBody deleteRequest: DeleteRequest, principal: Principal){
        try{
            val user = userService.findByEmail(principal.name)
            fileStorage.deleteFile(deleteRequest , user)
        }catch (e: Exception){
            throw e
        }
    }

    @PostMapping("/copyfile")
    @PreAuthorize("isAuthenticated()")
    fun copyfile(@RequestBody copyRequest: CopyRequest, principal: Principal){
        val user = userService.findByEmail(principal.name)
        fileStorage.copyFile(copyRequest  ,user)
    }

    @PostMapping("/movefile")
    @PreAuthorize("isAuthenticated()")
    fun movefile(@RequestBody moveRequest: MoveRequest, principal: Principal){
        val user = userService.findByEmail(principal.name)
        fileStorage.moveFile(moveRequest,user)
    }

    @PostMapping("/toggleVisiblity")
    @PreAuthorize("isAuthenticated()")
    fun toggleVisiblity(@RequestBody filePath: String, principal: Principal){
        val user = userService.findByEmail(principal.name)
        fileStorage.toggleFileVisiblity(filePath , user)
    }

    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    fun search(@RequestParam("text") text: String, principal: Principal): List<FileEntity> {
        val user = userService.findByEmail(principal.name)
        return fileStorage.search(text, user)
    }

}