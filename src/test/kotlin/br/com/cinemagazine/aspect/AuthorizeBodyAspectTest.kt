package br.com.cinemagazine.aspect

import br.com.cinemagazine.builder.watch.getWatchRequestDTO
import br.com.cinemagazine.constants.ApiMessage.USER_UNAUTHORIZED
import br.com.cinemagazine.controllers.WatchController
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
import org.aspectj.lang.reflect.MethodSignature
import kotlin.reflect.jvm.javaMethod

class AuthorizeBodyAspectTest: FunSpec({

    val httpRequest = mockk<HttpServletRequest>()
    val tokenService = mockk<TokenService>()
    val joinPoint = mockk<JoinPoint>()
    val methodSignature = mockk<MethodSignature>()
    val authorizeBody = AuthorizeBodyAspect(httpRequest, tokenService)

    afterTest { clearAllMocks() }

    test("when the body id is not the same as the token id, it must return error") {
        val method = WatchController::addWatchProduction.javaMethod
        every { joinPoint.signature } returns methodSignature
        every { methodSignature.method } returns method
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "1"
        every { joinPoint.args } returns arrayOf(getWatchRequestDTO())

        val exception = shouldThrow<BusinessException> {
            authorizeBody.authorize(joinPoint)
        }
        exception.message.shouldBe(USER_UNAUTHORIZED.description)
    }

    test("should verify the information in the request body successfully") {
        val method = WatchController::addWatchProduction.javaMethod
        every { joinPoint.signature } returns methodSignature
        every { methodSignature.method } returns method
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "10"
        every { joinPoint.args } returns arrayOf(getWatchRequestDTO())

        shouldNotThrow<BusinessException> { authorizeBody.authorize(joinPoint) }
        verify { tokenService.getByField(any(String::class), any(String::class)) }
    }
})