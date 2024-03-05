package br.com.cinemagazine.services

import br.com.cinemagazine.builder.tmdb.getPageTMDB
import br.com.cinemagazine.builder.tmdb.getProductionTMDB
import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.services.impl.TrendingServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class TrendingServiceTest: FunSpec({

    val proxy = mockk<TMDBProxy>()
    val service = TrendingServiceImpl(proxy)

    val hostImages = "https://image.tmdb.org/t/p/original"

    afterTest { clearAllMocks() }

    test("should return productions trending with successful") {
        val responseOne = listOf(getProductionTMDB(), getProductionTMDB(media = "person"))
        val responseTwo = listOf(getProductionTMDB("New Test", "tv"))
        every { proxy.trending(1) } returns getPageTMDB(list = responseOne)
        every { proxy.trending(2) } returns getPageTMDB(2, responseTwo)

        val result = service.getTrending()

        result.size.shouldBe(2)
        result[0].title.shouldBe(responseOne[0].title)
        result[1].title.shouldBe(responseTwo[0].title)
        result[0].media.shouldBe(responseOne[0].media)
        result[1].media.shouldBe(responseTwo[0].media)
        result[0].poster?.contains(hostImages)
        result[1].poster?.contains(hostImages)
    }
})