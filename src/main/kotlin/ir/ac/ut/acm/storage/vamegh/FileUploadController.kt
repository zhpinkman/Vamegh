package ir.ac.ut.acm.storage.vamegh

import ir.ac.ut.acm.storage.vamegh.Services.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.multipart.MultipartFile
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController
import java.security.Principal


@RestController
class FileUploadController {
    @Autowired
    lateinit var fileStorage: FileStorageService


    @PostMapping ("/upload")
    @PreAuthorize("isAuthenticated()")
    fun uploadMultiFile(@RequestParam("file") file: MultipartFile, principal: Principal, path: String){
//        val user = userService.findByEmail(principal.name)
//        fileStorage.store(file, user, path);
    }
}