package br.com.cinemagazine.controllers.unit

import br.com.cinemagazine.builder.production.getSearchDTO
import br.com.cinemagazine.services.SearchService
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import org.springframework.http.HttpStatus

class SearchControllerUnitTest: FunSpec({

    val searchService = mockk<SearchService>()
    val searchController = SearchController(searchService)

    afterTest { clearAllMocks() }

    test("should successfully return the search for productions") {
        val search = listOf(getSearchDTO())
        every { searchService.search(any(String::class)) } returns search

        val result = searchController.search("test")

        result.statusCode.shouldBe(HttpStatus.OK)
        result.body.shouldBe(search)
    }
})