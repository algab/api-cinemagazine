package br.com.cinemagazine.exception.handler

import br.com.cinemagazine.constants.ApiMessage
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.exception.ExceptionResponse
import br.com.cinemagazine.exception.FeignException
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler
    fun handleBusinessException(exception: BusinessException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(exception.status)
            .body(ExceptionResponse(exception.status.value(), exception.status.reasonPhrase, listOf(exception.message)))
    }

    @ExceptionHandler
    fun handleFeignException(exception: FeignException): ResponseEntity<ExceptionResponse> {
        return ResponseEntity.status(exception.status)
            .body(ExceptionResponse(exception.status.value(), exception.status.reasonPhrase, listOf(exception.message)))
    }

    @ExceptionHandler
    fun handleValidateBody(exception: MethodArgumentNotValidException): ResponseEntity<ExceptionResponse> {
        val message = exception.fieldErrors.map { it.defaultMessage }
        return ResponseEntity.status(BAD_REQUEST)
            .body(ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase, message))
    }

    @ExceptionHandler
    fun handleMethodArgumentTypeMismatchException(exception: MethodArgumentTypeMismatchException): ResponseEntity<ExceptionResponse> {
        val message = String.format(ApiMessage.FAILED_CONVERT_TYPE.description, exception.name)
        return ResponseEntity.status(BAD_REQUEST)
            .body(ExceptionResponse(BAD_REQUEST.value(), BAD_REQUEST.reasonPhrase, listOf(message)))
    }
}