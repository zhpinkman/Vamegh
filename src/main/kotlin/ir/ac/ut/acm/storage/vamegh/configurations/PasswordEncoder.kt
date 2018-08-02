package ir.ac.ut.acm.storage.vamegh.configurations

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component


@Component
class PasswordEncoder : BCryptPasswordEncoder()
