package ir.ac.ut.acm.storage.vamegh.services.userService

import ir.ac.ut.acm.storage.vamegh.exceptions.EntityNotFound
import ir.ac.ut.acm.storage.vamegh.exceptions.InvalidValueException
import ir.ac.ut.acm.storage.vamegh.configurations.PasswordEncoder
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.RegisterRequest
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.exceptions.NotUniqueException
import ir.ac.ut.acm.storage.vamegh.repositories.UserRepository
import ir.ac.ut.acm.storage.vamegh.services.fileService.FileStorageService
import ir.ac.ut.acm.storage.vamegh.services.mailService.MailClientService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DuplicateKeyException
import org.springframework.stereotype.Service
import org.springframework.web.util.UriComponentsBuilder


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

    override fun findById(id: String): User {
        return userRepository.findById(id).get()
    }

    override fun updateUser(user: User) {
        userRepository.save(user)
    }

    override fun register(registerRequest: RegisterRequest){
        try {
            val emailPattern = "[^ @+-]*@(ut.ac.ir)$"
            val passwordPattern = "^(?=.*[0-9]).{4,}$"
            if (!emailPattern.toRegex().matches(registerRequest.email) )
                throw InvalidValueException("email you entered is not valid!!")
            if (!passwordPattern.toRegex().matches(registerRequest.password) )
                throw InvalidValueException("password you entered is not valid!!")
            val passwordEncoded = passwordEncoder.encode(registerRequest.password)
            val user = User(email = registerRequest.email.toLowerCase(), bucketName = registerRequest.bucketName, password = passwordEncoded)
            fileStorageService.mkDir(  name = registerRequest.bucketName ,parentPath = "/")
            userRepository.save(user)
            sendVerificationMail(user)

        }
        catch (e: DuplicateKeyException) {
            throw NotUniqueException("Chosen Email or Bucket name is not unique")
        }

    }

    @Autowired
    private lateinit var mailClientService: MailClientService
    override fun sendVerificationMail(user: User) {
        user.token.reGenerate()
        updateUser(user)
        val link: UriComponentsBuilder = UriComponentsBuilder.newInstance()
                .scheme("http").host("localhost").port(4200).path("/activation").queryParam("userId" , user.id).queryParam("token" , user.token.value)
        link.build()
        mailClientService.prepareAndSend(user.email , link.toUriString())
    }

}