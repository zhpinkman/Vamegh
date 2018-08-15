package ir.ac.ut.acm.storage.vamegh.controllers.file


import ir.ac.ut.acm.storage.vamegh.controllers.file.models.DeleteRequest
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.FileList
import ir.ac.ut.acm.storage.vamegh.controllers.file.models.RenameRequest
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
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

        }
    }
}