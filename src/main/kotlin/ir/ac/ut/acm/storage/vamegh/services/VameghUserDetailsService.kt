package ir.ac.ut.acm.storage.vamegh.services

import ir.ac.ut.acm.storage.vamegh.entities.MyUserDetails
import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class VameghUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userService: UserService

    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username == null)
            return null
        val user = userService.findByEmail(username)?: throw ClassNotFoundException("User $username not found")
        val userDetails = MyUserDetails()
        userDetails._username = user.email!!
        userDetails._password = user.password!!
        userDetails._userId = user.id!!
        return userDetails
    }
}