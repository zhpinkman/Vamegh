package ir.ac.ut.acm.storage.vamegh.services.userService

import ir.ac.ut.acm.storage.vamegh.Exeptions.EntityNotFound
import ir.ac.ut.acm.storage.vamegh.Exeptions.NotUniqueException
import ir.ac.ut.acm.storage.vamegh.configurations.PasswordEncoder
import ir.ac.ut.acm.storage.vamegh.controllers.user.models.RegisterRequest
import ir.ac.ut.acm.storage.vamegh.entities.User
import ir.ac.ut.acm.storage.vamegh.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    override fun findByEmail(email: String) : User {
       return userRepository.findByEmail(email) ?: throw EntityNotFound("user with email: $email not found")
    }

    override fun register(registerRequest: RegisterRequest){
        try {
            val passwordEncoded = passwordEncoder.encode(registerRequest.password)
            val user = User(email = registerRequest.email, bucketName = registerRequest.bucketName, password = passwordEncoded)
            userRepository.save(user)
        }catch (e: Exception){
            throw NotUniqueException()
        }

    }

}