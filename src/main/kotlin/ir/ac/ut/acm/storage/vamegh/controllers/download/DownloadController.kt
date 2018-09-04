package ir.ac.ut.acm.storage.vamegh.controllers.download

import ir.ac.ut.acm.storage.vamegh.exceptions.EntityNotFound
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
class DownloadController {

    @Autowired
    lateinit var fileStorage: FileStorageService

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/*/*/**")
//    @PreAuthorize("isAuthenticated()")
    fun download(request: HttpServletRequest, response: HttpServletResponse) {
//        val user = userService.findByEmail(principal.name)
        val path = request.requestURI
        if (fileStorage.existsAndIsAllowed(path , null))
            response.setHeader("X-Accel-Redirect" , "/cloud$path")
        else
            throw EntityNotFound("file not found")
    }
}