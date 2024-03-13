package br.com.cinemagazine.clients

import br.com.cinemagazine.builder.tmdb.getCreditMovieTMDB
import br.com.cinemagazine.builder.tmdb.getCreditTvTMDB
import br.com.cinemagazine.builder.tmdb.getMovieTMDB
import br.com.cinemagazine.builder.tmdb.getPageTMDB
import br.com.cinemagazine.builder.tmdb.getSearchTMDB
import br.com.cinemagazine.builder.tmdb.getTrendingTMDB
import br.com.cinemagazine.builder.tmdb.getTvTMDB
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class TMDBProxyTest: FunSpec({

    val apiKey = "API_KEY"
    val language = "PT_BR"
    val client = mockk<TMDBClient>()
    val proxy = TMDBProxy(client, apiKey, language)

    afterTest { clearAllMocks() }

    test("should return productions trending with successful") {
        val trending = listOf(getTrendingTMDB())
        every { client.trending("day", apiKey, language, 1) } returns getPageTMDB(list = trending)

        val result = proxy.trending(1)

        result.page.shouldBe(1)
        result.results.size.shouldBe(1)
    }

    test("should search movie with successful") {
        val search = listOf(getSearchTMDB())
        every { client.searchMovie(apiKey, language, "test") } returns getPageTMDB(list = search)

        val result = proxy.searchMovie("test")

        result.page.shouldBe(1)
        result.results[0].title.shouldBe(search[0].title)
    }

    test("should search tv with successful") {
        val search = listOf(getSearchTMDB())
        every { client.searchTV(apiKey, language, "test") } returns getPageTMDB(list = search)

        val result = proxy.searchTV("test")

        result.page.shouldBe(1)
        result.results[0].title.shouldBe(search[0].title)
    }

    test("should return production of type movie") {
        val movie = getMovieTMDB()
        every { client.getMovie(1, apiKey, language) } returns movie

        val result = proxy.getMovie(1)

        result.shouldBe(movie)
    }

    test("should return credits of movie") {
        val credits = getCreditMovieTMDB()
        every { client.getMovieCredits(1, apiKey, language) } returns credits

        val result = proxy.getMovieCredits(1)

        result.shouldBe(credits)
    }

    test("should return production of type tv") {
        val tv = getTvTMDB()
        every { client.getTV(1, apiKey, language) } returns tv

        val result = proxy.getTV(1)

        result.shouldBe(tv)
    }

    test("should return credits of tv") {
        val credits = getCreditTvTMDB()
        every { client.getTVCredits(1, apiKey, language) } returns credits

        val result = proxy.getTVCredits(1)

        result.shouldBe(credits)
    }
})