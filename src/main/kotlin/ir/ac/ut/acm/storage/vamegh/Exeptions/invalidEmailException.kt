package ir.ac.ut.acm.storage.vamegh.Exeptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.BAD_REQUEST)
class invalidEmailException(message: String): Exception(message)