package br.com.cinemagazine.exception

import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.exception.handler.ExceptionHandler
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException

class ExceptionHandlerTest: FunSpec({

    val methodArgumentException = mockk<MethodArgumentNotValidException>()
    val handler = ExceptionHandler()

    test("should successfully test the manipulation of the BusinessException") {
        val businessException = BusinessException(CONFLICT, EMAIL_ALREADY_EXISTS)

        val result = handler.handleBusinessException(businessException)

        result.statusCode.shouldBe(CONFLICT)
        result.body?.status.shouldBe(CONFLICT.value())
        result.body?.error.shouldBe(CONFLICT.reasonPhrase)
        result.body?.description.shouldBe(listOf(EMAIL_ALREADY_EXISTS.description))
    }

    test("should successfully test the manipulation of the MethodArgumentNotValidException") {
        every { methodArgumentException.fieldErrors } returns listOf(FieldError("", "", "test"))

        val result = handler.handleValidateBody(methodArgumentException)

        result.statusCode.shouldBe(BAD_REQUEST)
        result.body?.status.shouldBe(BAD_REQUEST.value())
        result.body?.error.shouldBe(BAD_REQUEST.reasonPhrase)
        result.body?.description.shouldBe(listOf("test"))
    }
})