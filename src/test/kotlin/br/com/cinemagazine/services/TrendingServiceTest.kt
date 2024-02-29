package br.com.cinemagazine.services

import br.com.cinemagazine.builder.tmdb.getTrendingTMDB
import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.services.impl.TrendingServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TrendingServiceTest: FunSpec({

    val hostImages = "https://image.tmdb.org/t/p/original"
    val proxy = mockk<TMDBProxy>()
    val service = TrendingServiceImpl(proxy)

    test("should return productions trending with successful") {
        val trending = listOf(getTrendingTMDB())
        every { proxy.getTrending() } returns trending

        val result = service.getTrending()

        result[0].id.shouldBe(trending[0].id)
        result[0].title.shouldBe(trending[0].title)
        result[0].originalTitle.shouldBe(trending[0].originalTitle)
        result[0].description.shouldBe(trending[0].description)
        result[0].dateRelease.shouldBe(trending[0].dateRelease)
        result[0].media.shouldBe(trending[0].media)
        result[0].poster.contains(hostImages)
    }
})