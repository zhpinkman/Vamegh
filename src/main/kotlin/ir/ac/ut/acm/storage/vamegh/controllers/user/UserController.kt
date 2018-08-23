package ir.ac.ut.acm.storage.vamegh.controllers.user


import ir.ac.ut.acm.storage.vamegh.controllers.user.models.ActivationRequest
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.RegisterRequest
import ir.ac.ut.acm.storage.vamegh.entities.VerificationToken
import ir.ac.ut.acm.storage.vamegh.exceptions.ExpiredException
import ir.ac.ut.acm.storage.vamegh.exceptions.InvalidValueException
import ir.ac.ut.acm.storage.vamegh.services.mailService.MailClientService
import ir.ac.ut.acm.storage.vamegh.services.userService.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import java.security.Principal



@RestController
@RequestMapping("/user")
class UserController {
    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var mailClientService: MailClientService

    @PostMapping("/register")
    fun register(@RequestBody request: RegisterRequest) {
        userService.register(request)
    }

    @GetMapping("/resendverif")
    fun reSendActivationLink(@RequestHeader email: String){
        val user = userService.findByEmail(email)
        userService.sendVerificationMail(user)
    }

    @PostMapping("/activate")
    fun activate(@RequestBody acivationRequest: ActivationRequest){
        val user = userService.findById(acivationRequest.userId)
        val verificationResult = user.token.isValid(acivationRequest.token)
        when (verificationResult) {
            0 -> {
                user.active = true
                userService.updateUser(user)}
            -1 -> throw ExpiredException("Verification token is created before 2 days ago")
            -2 -> throw InvalidValueException("Token value is not valid")
        }
    }

}