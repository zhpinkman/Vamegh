package ir.ac.ut.acm.storage.vamegh.controllers.file


import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.bind.annotation.RestController
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
    fun uploadMultiFile(@RequestParam("file") file: MultipartFile,@RequestParam("path") path: String , principal: Principal){
        val user = userService.findByEmail(principal.name)
        fileStorage.store(file, user, path);
    }
}