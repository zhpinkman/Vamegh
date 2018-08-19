package ir.ac.ut.acm.storage.vamegh.controllers.download

import ir.ac.ut.acm.storage.vamegh.exceptions.EntityNotFound
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@RestController
@RequestMapping("download")
class DownloadController {

    @Autowired
    lateinit var fileStorage: FileStorageService

    @Autowired
    lateinit var userService: UserService

    @GetMapping("/**")
    @PreAuthorize("isAuthenticated()")
    fun download(request: HttpServletRequest, response: HttpServletResponse, principal: Principal) {
        val user = userService.findByEmail(principal.name)
        // if(request.contextPath.substringAfterLast('/')===user.bucketName){
        val path = request.requestURI
        if (fileStorage.exists(path)) {
            response.sendRedirect("http://" + request.serverName + "/DownloadController" + request.requestURI)
        } else {
            throw EntityNotFound("file not found")
        }
        //}
    }
}