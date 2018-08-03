package ir.ac.ut.acm.storage.vamegh.services.userService

import ir.ac.ut.acm.storage.vamegh.controllers.user.models.RegisterRequest
import ir.ac.ut.acm.storage.vamegh.entities.User

interface UserService {
    fun findByEmail(email: String) : User
    fun register(registerRequest: RegisterRequest)
}