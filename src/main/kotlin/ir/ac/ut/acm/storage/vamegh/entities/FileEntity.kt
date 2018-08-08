package ir.ac.ut.acm.storage.vamegh.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.*

@Document(collection = "filesData")
class FileEntity (
        @Id val id: String? = null,
        var name: String,
        val size: Long,
        var parentId: String,
        var path: String,
        val creationDate: Date,
        val isDir: Boolean
)