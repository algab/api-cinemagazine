package br.com.cinemagazine.aspect

import br.com.cinemagazine.builder.document.getWatchedDocument
import br.com.cinemagazine.constants.ApiMessage.USER_UNAUTHORIZED
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.WatchedRepository
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
import java.util.Optional

class AuthorizeWatchedAspectTest: FunSpec({

    val httpRequest = mockk<HttpServletRequest>()
    val tokenService = mockk<TokenService>()
    val watchedRepository = mockk<WatchedRepository>()
    val joinPoint = mockk<JoinPoint>()
    val authorizeWatched = AuthorizeWatchedAspect(httpRequest, tokenService, watchedRepository)

    afterTest { clearAllMocks() }

    test("when the record is not that of the token user, it must return an error") {
        every { joinPoint.args } returns arrayOf("1")
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "1"
        every { watchedRepository.findById(any(String::class)) } returns Optional.of(getWatchedDocument())

        val exception = shouldThrow<BusinessException> {
            authorizeWatched.authorize(joinPoint)
        }
        exception.message.shouldBe(USER_UNAUTHORIZED.description)
    }

    test("should check successfully when the ids are equal") {
        every { joinPoint.args } returns arrayOf("1")
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "1010"
        every { watchedRepository.findById(any(String::class)) } returns Optional.of(getWatchedDocument())

        shouldNotThrow<BusinessException> { authorizeWatched.authorize(joinPoint) }
        verify { tokenService.getByField(any(String::class), any(String::class)) }
        verify { watchedRepository.findById(any(String::class)) }
    }

    test("should check successfully when record is empty") {
        every { joinPoint.args } returns arrayOf("1")
        every { httpRequest.getHeader(any(String::class)) } returns "Bearer 1234"
        every { tokenService.getByField(any(String::class), any(String::class)) } returns "1"
        every { watchedRepository.findById(any(String::class)) } returns Optional.empty()

        shouldNotThrow<BusinessException> { authorizeWatched.authorize(joinPoint) }
        verify { tokenService.getByField(any(String::class), any(String::class)) }
        verify { watchedRepository.findById(any(String::class)) }
    }
})