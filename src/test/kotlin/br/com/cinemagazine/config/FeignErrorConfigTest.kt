package br.com.cinemagazine.config

import feign.Response
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class FeignErrorConfigTest: FunSpec({

    val httpResponse = mockk<Response>()
    val feignErrorConfig = FeignErrorConfig()

    test("should execute method decode with successfully") {
        val message = "Internal Server Error"
        every { httpResponse.status() } returns 500
        every { httpResponse.reason() } returns message

        val exception = feignErrorConfig.decode("", httpResponse)
        exception.message.shouldBe(message)
    }
})