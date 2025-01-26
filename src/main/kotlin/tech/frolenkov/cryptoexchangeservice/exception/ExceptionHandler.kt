package tech.frolenkov.cryptoexchangeservice.exception

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ExceptionHandler {

    @ExceptionHandler(NotFoundException::class)
    fun entityNotFoundHandler(ex: NotFoundException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(HttpStatus.NOT_FOUND.value(), ex.localizedMessage),
            HttpStatusCode.valueOf(404)
        )

    @ExceptionHandler(ConflictException::class)
    fun entityConflictExceptionExistHandler(ex: ConflictException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(HttpStatus.BAD_REQUEST.value(), ex.localizedMessage),
            HttpStatusCode.valueOf(400)
        )

    @ExceptionHandler(AuthenticationException::class)
    fun entityAllReadyExistHandler(ex: AuthenticationException): ResponseEntity<ExceptionResponse> =
        ResponseEntity(
            ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), ex.localizedMessage),
            HttpStatusCode.valueOf(401)
        )

}