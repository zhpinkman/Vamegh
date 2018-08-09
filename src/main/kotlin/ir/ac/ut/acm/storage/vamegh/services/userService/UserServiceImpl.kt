package ir.ac.ut.acm.storage.vamegh.services.userService
import ir.ac.ut.acm.storage.vamegh.configurations.PasswordEncoder
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.RegisterRequest
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.exceptions.EntityNotFound
import ir.ac.ut.acm.storage.vamegh.exceptions.InvalidEmailException
import ir.ac.ut.acm.storage.vamegh.repositories.UserRepository
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service


class EmailValidator {
    companion object {
        @JvmStatic val EMAIL_REGEX = "[^ @+-]*@(ut.ac.ir)$"
        fun isEmailValid(email: String): Boolean {
            return EMAIL_REGEX.toRegex().matches(email)
        }
    }
}


@Service
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder
    @Autowired
    lateinit var fileStorageService: FileStorageService
    @Value("\${utCloud.storagePath}")
    lateinit var rootLocation: String

    override fun findByEmail(email: String) : User {
       return userRepository.findByEmail(email) ?: throw EntityNotFound("user with email: $email not found")
    }

    override fun register(registerRequest: RegisterRequest){
        try {
            if(!EmailValidator.isEmailValid(registerRequest.email))
                throw InvalidEmailException("email you entered is not valid!!")
            val passwordEncoded = passwordEncoder.encode(registerRequest.password)
            val user = User(email = registerRequest.email.toLowerCase(), bucketName = registerRequest.bucketName, password = passwordEncoded)
            userRepository.save(user)
            fileStorageService.mkDir(  name = registerRequest.bucketName ,parentPath = "/")
        }catch (e: Exception){
            throw e
        }

    }

}