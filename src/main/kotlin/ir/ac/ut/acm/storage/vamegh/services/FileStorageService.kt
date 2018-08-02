package ir.ac.ut.acm.storage.vamegh.services

import ir.ac.ut.acm.storage.vamegh.entities.User
import org.springframework.web.multipart.MultipartFile

interface FileStorageService{
    fun store (file:MultipartFile , user: User, path: String)
}