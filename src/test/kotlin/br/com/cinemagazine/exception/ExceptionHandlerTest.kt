package br.com.cinemagazine.exception

import br.com.cinemagazine.constants.ApiMessage.EMAIL_ALREADY_EXISTS
import br.com.cinemagazine.exception.handler.ExceptionHandler
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus.CONFLICT

class ExceptionHandlerTest: FunSpec({
    val handler = ExceptionHandler()

    test("test BusinessException") {
        val businessException = BusinessException(CONFLICT, EMAIL_ALREADY_EXISTS)

        val result = handler.handleBusinessException(businessException)

        result.statusCode.shouldBe(CONFLICT)
        result.body?.status.shouldBe(CONFLICT.value())
        result.body?.error.shouldBe(CONFLICT.reasonPhrase)
        result.body?.description.shouldBe(listOf(EMAIL_ALREADY_EXISTS.description))
    }
})