package ir.ac.ut.acm.storage.vamegh.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.io.Serializable


@Document(collection = "users")
class User (
        @Id
        val id: String? = null ,
        @Indexed(unique = true)
        val email: String,
        val password: String ) {

}