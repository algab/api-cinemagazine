package br.com.cinemagazine.clients

import br.com.cinemagazine.builder.tmdb.getPageTMDB
import br.com.cinemagazine.builder.tmdb.getProductionTMDB
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk

class TMDBProxyTest: FunSpec({

    val client = mockk<TMDBClient>()
    val proxy = TMDBProxy(client, "API_KEY", "PT_BR")

    test("should return productions trending with successful") {
        val requestOne = listOf(getProductionTMDB(), getProductionTMDB(media = "person"))
        val requestTwo = listOf(getProductionTMDB("New Test", "tv"))
        every { client.trending("day", "API_KEY", "PT_BR", 1) } returns getPageTMDB(list = requestOne)
        every { client.trending("day", "API_KEY", "PT_BR", 2) } returns getPageTMDB(2, requestTwo)

        val result = proxy.getTrending()

        result.size.shouldBe(2)
        result[0].title.shouldBe(requestOne[0].title)
        result[1].title.shouldBe(requestTwo[0].title)
    }
})