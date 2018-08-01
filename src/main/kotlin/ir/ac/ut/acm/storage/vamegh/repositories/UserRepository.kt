package ir.ac.ut.acm.storage.vamegh.repositories

import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User , String> {
    fun findByEmail(email: String) : User
}
