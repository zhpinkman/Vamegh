package ir.ac.ut.acm.storage.vamegh

import com.example.demo.FileStorage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.userdetails.UserDetailsService


@SpringBootApplication
class VameghApplication {

    @Autowired
    lateinit var userService: UserDetailsService
    @Autowired
    lateinit var fileStorage: FileStorage;

    @Autowired
    @Throws(Exception::class)
    fun authenticationManager(builder: AuthenticationManagerBuilder) {
        builder.userDetailsService<UserDetailsService>(userService)
    }

    @Bean
    fun run() = CommandLineRunner {
        fileStorage.deleteAll()
        fileStorage.init()
    }

}

fun main(args: Array<String>) {
    runApplication<VameghApplication>(*args)
}