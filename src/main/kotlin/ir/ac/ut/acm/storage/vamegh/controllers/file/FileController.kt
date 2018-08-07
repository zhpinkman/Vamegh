package ir.ac.ut.acm.storage.vamegh.controllers.file


import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.EmptyResultDataAccessException
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

    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String



    @PostMapping ("/upload")
    @PreAuthorize("isAuthenticated()")
    fun uploadMultiFile(@RequestParam("file") file: MultipartFile,@RequestParam("path") path: String = "" , principal: Principal){
        val user = userService.findByEmail(principal.name)
        fileStorage.store(file, user, path);
    }

    @PostMapping ("/list")
    @PreAuthorize("isAuthenticated()")
    fun getFilesList(@RequestParam("path") path: String , principal: Principal): List<FileEntity>{
        val user = userService.findByEmail(principal.name)
        val completePath = rootLocation + "/" + user.bucketName + "/" + path
        return fileStorage.getFilesList(completePath)
    }

    @PostMapping ("/rename")
    @PreAuthorize("isAuthenticated()")
    fun rename(@RequestParam("parentPath") parentPath: String, @RequestParam("oldName") oldName: String, @RequestParam("newName") newName: String , principal: Principal) {
        try {
            val user = userService.findByEmail(principal.name)
            val completeParentPath = rootLocation + "/" + user.bucketName + parentPath
            fileStorage.renameFile(parentPath = completeParentPath , newFileName = newName , oldFileName = oldName)
        }catch (e: Exception){
//            if(e != EmptyResultDataAccessException)
        }
    }
}