package br.com.cinemagazine.aspect

import br.com.cinemagazine.constants.ApiMessage.USER_UNAUTHORIZED
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
import org.aspectj.lang.JoinPoint

class AuthorizeUserAspectTest: FunSpec({

    val httpRequest = mockk<HttpServletRequest>()
    val tokenService = mockk<TokenService>()
    val joinPoint = mockk<JoinPoint>()
    val authorizeUser = AuthorizeUserAspect(httpRequest, tokenService)

    afterTest { clearAllMocks() }

    test("when the user id is not the same as the token id, it must return error") {
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "1"
        every { joinPoint.args } returns arrayOf("10")

        val exception = shouldThrow<BusinessException> {
            authorizeUser.authorize(joinPoint)
        }
        exception.message.shouldBe(USER_UNAUTHORIZED.description)
    }

    test("should verify user with successfully") {
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "1"
        every { joinPoint.args } returns arrayOf("1")

        shouldNotThrow<BusinessException> { authorizeUser.authorize(joinPoint) }
        verify { tokenService.getByField(any(String::class), any(String::class)) }
    }
})