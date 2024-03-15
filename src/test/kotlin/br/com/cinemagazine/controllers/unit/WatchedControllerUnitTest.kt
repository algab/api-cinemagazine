package br.com.cinemagazine.controllers.unit

import br.com.cinemagazine.builder.watched.getWatchedDTO
import br.com.cinemagazine.builder.watched.getWatchedRequestDTO
import br.com.cinemagazine.controllers.WatchedController
import br.com.cinemagazine.dto.watched.WatchedRequestDTO
import br.com.cinemagazine.services.WatchedService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus

class WatchedControllerUnitTest: FunSpec({

    val watchedService = mockk<WatchedService>()
    val watchedController = WatchedController(watchedService)

    afterEach { clearAllMocks() }

    test("should add watched production successfully") {
        every { watchedService.addWatchedProduction(any(WatchedRequestDTO::class)) } returns getWatchedDTO()

        val result = watchedController.addWatchedProduction(getWatchedRequestDTO())

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body.shouldBe(getWatchedDTO())
    }

    test("should get productions watched successfully") {
        every { watchedService.getWatchedProductions(any(String::class), any(Pageable::class)) } returns PageImpl(listOf(getWatchedDTO()))

        val result = watchedController.getWatchedProductions("1010", PageRequest.of(0, 10))

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.content.shouldBe(listOf(getWatchedDTO()))
    }

    test("should delete watched production successfully") {
        every { watchedService.deleteWatchedProduction(any(String::class)) } returns Unit

        val result = watchedController.deleteWatchedProduction("1010")

        result.statusCode.shouldBe(HttpStatus.NO_CONTENT)
    }
})