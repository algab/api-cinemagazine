package br.com.cinemagazine.controllers.unit

import br.com.cinemagazine.builder.production.getMovieDTO
import br.com.cinemagazine.builder.production.getSearchDTO
import br.com.cinemagazine.builder.production.getTrendingDTO
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.controllers.ProductionController
import br.com.cinemagazine.services.ProductionService
import br.com.cinemagazine.services.SearchService
import br.com.cinemagazine.services.TrendingService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class ProductionControllerUnitTest: FunSpec({

    val trendingService = mockk<TrendingService>()
    val searchService = mockk<SearchService>()
    val productionService = mockk<ProductionService>()
    val productionController = ProductionController(trendingService, searchService, productionService)

    afterTest { clearAllMocks() }

    test("should return productions trending with successful") {
        val trending = listOf(getTrendingDTO())
        every { trendingService.getTrending() } returns trending

        val result = productionController.getTrending()

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body?.shouldBe(trending)
    }

    test("should successfully return the search for productions") {
        val search = listOf(getSearchDTO())
        every { searchService.search(any(String::class)) } returns search

        val result = productionController.search("test")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body.shouldBe(search)
    }

    test("should return production information successfully") {
        val production = getMovieDTO()
        every { productionService.getProduction(any(Long::class), any(Media::class)) } returns production

        val result = productionController.getProduction(1, Media.MOVIE)

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body.shouldBe(production)
    }
})