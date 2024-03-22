package br.com.cinemagazine.aspect

import br.com.cinemagazine.constants.ApiMessage.TOKEN_NOT_FOUND
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.services.TokenService
import io.kotest.assertions.throwables.shouldNotThrow
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders.AUTHORIZATION

class AuthorizeAspectTest: FunSpec({

    val httpRequest = mockk<HttpServletRequest>()
    val tokenService = mockk<TokenService>()
    val authorizeAspect = AuthorizeAspect(httpRequest, tokenService)

    afterTest { clearAllMocks() }

    test("when not sending the authorization header it should return an error") {
        every { httpRequest.getHeader(AUTHORIZATION) } returns null

        val exception = shouldThrow<BusinessException> {
            authorizeAspect.authorize()
        }
        exception.message.shouldBe(TOKEN_NOT_FOUND.description)
    }

    test("should authorize token with successful") {
        every { httpRequest.getHeader(AUTHORIZATION) } returns "Bearer token"
        every { tokenService.validateAccessToken(any(String::class)) } returns Unit

        shouldNotThrow<BusinessException> { authorizeAspect.authorize() }
        verify { tokenService.validateAccessToken(any(String::class)) }
    }
})