package ir.ac.ut.acm.storage.vamegh.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.CONFLICT)
class DirectoryWithSameNameExists (message: String):Exception(message)