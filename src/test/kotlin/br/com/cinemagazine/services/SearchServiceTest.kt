package br.com.cinemagazine.services

import br.com.cinemagazine.builder.tmdb.getPageTMDB
import br.com.cinemagazine.builder.tmdb.getSearchTMDB
import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.services.impl.SearchServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class SearchServiceTest: FunSpec({

    val proxy = mockk<TMDBProxy>()
    val service = SearchServiceImpl(proxy)

    val hostImage = "https://image.tmdb.org/t/p/original"

    afterTest { clearAllMocks() }

    test("should search productions with successful") {
        val responseMovie = listOf(getSearchTMDB(popularity = 10))
        val responseTV = listOf(getSearchTMDB("New Test", 20))
        every { proxy.searchMovie("test") } returns getPageTMDB(list = responseMovie)
        every { proxy.searchTV("test") } returns getPageTMDB(list = responseTV)

        val result = service.search("test")

        result.size.shouldBe(2)
        result[0].title.shouldBe(responseTV[0].title)
        result[1].title.shouldBe(responseMovie[0].title)
        result[0].media.shouldBe(Media.TV.value)
        result[1].media.shouldBe(Media.MOVIE.value)
        result[0].poster?.contains(hostImage)
        result[1].poster?.contains(hostImage)
    }
})