package ir.ac.ut.acm.storage.vamegh.controllers.file.models

import ir.ac.ut.acm.storage.vamegh.entities.FileEntity
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import java.util.*

class FileInfo(fileEntity: FileEntity , url: String){
    val id: String? = fileEntity.id
    var type: String = fileEntity.type
    var name: String = fileEntity.name
    val size: Long = fileEntity.size
    var parentId: String? = fileEntity.parentId
    var path: String = fileEntity.path
    val creationDate: Date = fileEntity.creationDate
    val isDir: Boolean = fileEntity.isDir
    val isPublic: Boolean = fileEntity.isPublic
    val url = if(isDir) null else url
}