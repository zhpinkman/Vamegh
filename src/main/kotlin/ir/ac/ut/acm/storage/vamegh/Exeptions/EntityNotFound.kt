package ir.ac.ut.acm.storage.vamegh.Exeptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class EntityNotFound(message: String): Exception(message)