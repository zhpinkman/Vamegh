package ir.ac.ut.acm.storage.vamegh.controllers.user

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @PreAuthorize("isAuthenticated()")
    @GetMapping
    fun str(): String {
        return "hello world";
    }

}