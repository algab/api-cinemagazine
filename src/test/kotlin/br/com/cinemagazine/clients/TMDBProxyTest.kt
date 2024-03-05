package br.com.cinemagazine.clients

import br.com.cinemagazine.builder.tmdb.getPageTMDB
import br.com.cinemagazine.builder.tmdb.getProductionTMDB
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk

class TMDBProxyTest: FunSpec({

    val client = mockk<TMDBClient>()
    val proxy = TMDBProxy(client, "API_KEY", "PT_BR")

    afterTest { clearAllMocks() }

    test("should return productions trending with successful") {
        val trending = listOf(getProductionTMDB())
        every { client.trending("day", "API_KEY", "PT_BR", 1) } returns getPageTMDB(list = trending)

        val result = proxy.trending(1)

        result.page.shouldBe(1)
        result.results.size.shouldBe(1)
    }

    test("should search movie with successful") {
        val search = listOf(getProductionTMDB())
        every { client.searchMovie("API_KEY", "PT_BR", "test") } returns getPageTMDB(list = search)

        val result = proxy.searchMovie("test")

        result.page.shouldBe(1)
        result.results[0].title.shouldBe(search[0].title)
    }

    test("should search tv with successful") {
        val search = listOf(getProductionTMDB())
        every { client.searchTV("API_KEY", "PT_BR", "test") } returns getPageTMDB(list = search)

        val result = proxy.searchTV("test")

        result.page.shouldBe(1)
        result.results[0].title.shouldBe(search[0].title)
    }
})