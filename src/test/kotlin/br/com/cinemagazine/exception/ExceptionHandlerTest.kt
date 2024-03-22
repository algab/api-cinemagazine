package br.com.cinemagazine.exception

import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.constants.ApiMessage.FAILED_CONVERT_TYPE
import br.com.cinemagazine.exception.handler.ExceptionHandler
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

class ExceptionHandlerTest: FunSpec({

    val methodArgumentNotValidException = mockk<MethodArgumentNotValidException>()
    val methodArgumentTypeMismatchException = mockk<MethodArgumentTypeMismatchException>()
    val handler = ExceptionHandler()

    test("should successfully test the manipulation of the BusinessException") {
        val businessException = BusinessException(CONFLICT, EMAIL_ALREADY_EXISTS)

        val result = handler.handleBusinessException(businessException)

        result.statusCode.shouldBe(CONFLICT)
        result.body?.status.shouldBe(CONFLICT.value())
        result.body?.error.shouldBe(CONFLICT.reasonPhrase)
        result.body?.description.shouldBe(listOf(EMAIL_ALREADY_EXISTS.description))
    }

    test("should successfully test the manipulation of the FeignException") {
        val message = "Internal Server Error"
        val feignException = FeignException(INTERNAL_SERVER_ERROR, message)

        val result = handler.handleFeignException(feignException)

        result.statusCode.shouldBe(INTERNAL_SERVER_ERROR)
        result.body?.status.shouldBe(INTERNAL_SERVER_ERROR.value())
        result.body?.error.shouldBe(INTERNAL_SERVER_ERROR.reasonPhrase)
        result.body?.description.shouldBe(listOf(message))
    }

    test("should successfully test the manipulation of the MethodArgumentNotValidException") {
        every { methodArgumentNotValidException.fieldErrors } returns listOf(FieldError("", "", "test"))

        val result = handler.handleValidateBody(methodArgumentNotValidException)

        result.statusCode.shouldBe(BAD_REQUEST)
        result.body?.status.shouldBe(BAD_REQUEST.value())
        result.body?.error.shouldBe(BAD_REQUEST.reasonPhrase)
        result.body?.description.shouldBe(listOf("test"))
    }

    test("should successfully test the manipulation of the MethodArgumentTypeMismatchException") {
        every { methodArgumentTypeMismatchException.name } returns "id"

        val result = handler.handleMethodArgumentTypeMismatchException(methodArgumentTypeMismatchException)

        result.statusCode.shouldBe(BAD_REQUEST)
        result.body?.status.shouldBe(BAD_REQUEST.value())
        result.body?.error.shouldBe(BAD_REQUEST.reasonPhrase)
        result.body?.description?.get(0)?.contains(FAILED_CONVERT_TYPE.description)
    }
})