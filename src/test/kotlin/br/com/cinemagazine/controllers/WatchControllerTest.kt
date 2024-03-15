package br.com.cinemagazine.controllers

import br.com.cinemagazine.builder.watch.getWatchDTO
import br.com.cinemagazine.builder.watch.getWatchRequestDTO
import br.com.cinemagazine.dto.watch.WatchRequestDTO
import br.com.cinemagazine.services.WatchService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus

class WatchControllerTest: FunSpec({

    val watchService = mockk<WatchService>()
    val watchController = WatchController(watchService)

    afterEach { clearAllMocks() }

    test("should add watch a production successfully") {
        every { watchService.addWatchProduction(any(WatchRequestDTO::class)) } returns getWatchDTO()

        val result = watchController.addWatchProduction(getWatchRequestDTO())

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body.shouldBe(getWatchDTO())
    }

    test("should get production to watch successfully") {
        every { watchService.getWatchProductions(any(String::class), any(Pageable::class)) } returns PageImpl(listOf(getWatchDTO()))

        val result = watchController.getWatchProductions("10", PageRequest.of(0, 10))

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.content.shouldBe(listOf(getWatchDTO()))
    }

    test("should delete production to watch successfully") {
        every { watchService.deleteWatchProduction(any(String::class)) } returns Unit

        val result = watchController.deleteWatchProduction("10")

        result.statusCode.shouldBe(HttpStatus.NO_CONTENT)
    }
})