package ir.ac.ut.acm.storage.vamegh.repositories

import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : MongoRepository<FileEntity , String> {
    fun findByPath(path: String): FileEntity
    fun findAllByParentId(parentId: String): List<FileEntity>
}