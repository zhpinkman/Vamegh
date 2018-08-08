package ir.ac.ut.acm.storage.vamegh.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.BAD_REQUEST)
class InvalidEmailException(message: String): Exception(message)