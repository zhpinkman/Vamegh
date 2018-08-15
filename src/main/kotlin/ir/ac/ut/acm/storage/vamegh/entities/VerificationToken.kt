package ir.ac.ut.acm.storage.vamegh.entities

import org.apache.commons.lang.RandomStringUtils
import java.util.*

class VerificationToken {
    var creationDate: Date = Date()
    var value: String = RandomStringUtils.random(10 , true , true)

    fun isValid(t: String): Int {
        val TWODAYSINMS = 2 * 24 * 60 * 60 * 1000
        if( value == t ){
            if(creationDate.after(Date(System.currentTimeMillis() - TWODAYSINMS)))
                return 0
            else
                return -1
        }
        else
            return -2
    }

    fun reGenerate(){
        creationDate = Date()
        value = RandomStringUtils.random(10 , true , true)
    }
}