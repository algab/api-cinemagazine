package br.com.cinemagazine.exception.handler

import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.exception.ExceptionResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    @ExceptionHandler
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(exception.status)
            .body(ExceptionResponse(exception.status.value(), exception.status.reasonPhrase, exception.message))
    }

}