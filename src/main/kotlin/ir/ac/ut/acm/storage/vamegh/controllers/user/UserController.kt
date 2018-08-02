package ir.ac.ut.acm.storage.vamegh.controllers.user

import ir.ac.ut.acm.storage.vamegh.configurations.PasswordEncoder
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.RegisterRequest
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.repositories.UserRepository
import ir.ac.ut.acm.storage.vamegh.services.UserService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService
    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest) {
        userService.register(request)
    }

}