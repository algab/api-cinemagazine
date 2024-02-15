package br.com.cinemagazine.exception.handler

import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.exception.ExceptionResponse
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(exception.status)
            .body(ExceptionResponse(exception.status.value(), exception.status.reasonPhrase, listOf(exception.message)))
    }

    @ExceptionHandler
    fun handleValidateBody(exception: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val message = exception.fieldErrors.map { it.defaultMessage }
        return ResponseEntity.status(BAD_REQUEST)
            .body(ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase, message))
    }
}