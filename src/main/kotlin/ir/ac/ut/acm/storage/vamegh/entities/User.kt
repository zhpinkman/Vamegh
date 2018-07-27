package ir.ac.ut.acm.storage.vamegh.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed

data class User(
        @Id
        val id: String? = null,
        @Indexed(unique = true)
        val email: String,
        val password: String
)