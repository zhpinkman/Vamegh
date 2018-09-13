package ir.ac.ut.acm.storage.vamegh.repositories

import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface FileRepository : MongoRepository<FileEntity , String?> {
    fun findByPath(path: String): FileEntity?
    fun findAllByParentId(parentId: String): List<FileEntity>
    fun existsByPath(path: String): Boolean
    @Query("{'userId' : ?0, 'name' : {\$regex : '.*?1.*'}}')")
    fun findByUserIdAndRegexpName(userId: String, regexp: String): List<FileEntity>
}