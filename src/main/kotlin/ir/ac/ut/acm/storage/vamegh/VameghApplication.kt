package ir.ac.ut.acm.storage.vamegh

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class VameghApplication

fun main(args: Array<String>) {
    runApplication<VameghApplication>(*args)
//    SpringApplication.run(VameghApplication::class.java, *args)

}
