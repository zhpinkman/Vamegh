package ir.ac.ut.acm.storage.vamegh.entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.ArrayList

class MyUserDetails : UserDetails {
    var grants: Collection<GrantedAuthority> = ArrayList()
    var _username: String = ""
    var _password: String = ""
    var _userId: String = ""
    var _active: Boolean = false

    override fun getUsername(): String {
        return _username
    }

    override fun getPassword(): String {
        return _password
    }

    override fun isEnabled(): Boolean {
        return _active
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return grants.toMutableList()
    }
}