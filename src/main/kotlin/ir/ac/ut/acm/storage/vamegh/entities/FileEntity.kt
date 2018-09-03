package ir.ac.ut.acm.storage.vamegh.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "filesData")
class FileEntity (
        @Id val id: String? = null,
        var type: String,
        var name: String,
        val size: Long,
        var parentId: String?,
        @Indexed(unique = true)
        var path: String,
        val creationDate: Date,
        val isDir: Boolean,
        var isPublic: Boolean = false
)