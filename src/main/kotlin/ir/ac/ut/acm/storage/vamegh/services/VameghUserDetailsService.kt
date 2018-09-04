package ir.ac.ut.acm.storage.vamegh.services

import ir.ac.ut.acm.storage.vamegh.entities.MyUserDetails
import ir.ac.ut.acm.storage.vamegh.services.userService.UserServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class VameghUserDetailsService : UserDetailsService {

    @Autowired
    private lateinit var userService: UserServiceImpl

    override fun loadUserByUsername(username: String?): UserDetails? {
        if (username == null)
            return null
        val user = userService.findByEmail(username)
        val userDetails = MyUserDetails()
        userDetails._username = user.email
        userDetails._password = user.password
        userDetails._userId = user.id!!
        userDetails._active = user.active
        return userDetails
    }
}