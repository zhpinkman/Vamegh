package ir.ac.ut.acm.storage.vamegh

import ir.ac.ut.acm.storage.vamegh.services.FileStorageService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetailsService


@SpringBootApplication
class VameghApplication {

    @Autowired
    lateinit var userService: UserDetailsService

    @Autowired
    @Throws(Exception::class)
    fun authenticationManager(builder: AuthenticationManagerBuilder) {
        builder.userDetailsService<UserDetailsService>(userService)
    }

}

fun main(args: Array<String>) {
    runApplication<VameghApplication>(*args)
}