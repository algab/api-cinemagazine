package br.com.cinemagazine.controllers.unit

import br.com.cinemagazine.builder.production.getTrendingDTO
import br.com.cinemagazine.controllers.TrendingController
import br.com.cinemagazine.services.TrendingService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class TrendingControllerUnitTest: FunSpec({

    val trendingService = mockk<TrendingService>()
    val trendingController = TrendingController(trendingService)

    afterTest { clearAllMocks() }

    test("should return productions trending with successful") {
        val trending = listOf(getTrendingDTO())
        every { trendingService.getTrending() } returns trending

        val result = trendingController.getTrending()

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.shouldBe(trending)
    }
})