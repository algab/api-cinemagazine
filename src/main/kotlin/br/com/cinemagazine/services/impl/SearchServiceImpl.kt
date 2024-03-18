package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.SearchDTO
import br.com.cinemagazine.dto.tmdb.SearchTMDB
import br.com.cinemagazine.services.SearchService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(private val proxy: TMDBProxy): SearchService {
    override fun search(name: String) = runBlocking {
        val resultSearch = mutableListOf<SearchDTO>()
        val deferredSearch = awaitAll(*listOf(
            async { proxy.searchMovie(name) },
            async { proxy.searchTV(name) }
        ).toTypedArray())
        resultSearch.addAll(deferredSearch[0].results.map { mountProduction(it, Media.MOVIE.value) })
        resultSearch.addAll(deferredSearch[1].results.map { mountProduction(it, Media.TV.value) })
        resultSearch.sortedByDescending { it.popularity }
    }

    private fun mountProduction(production: SearchTMDB, type: String): SearchDTO {
        return SearchDTO(
            production.id,
            production.title,
            production.originalTitle,
            production.description,
            production.poster,
            production.dateRelease,
            type,
            production.popularity
        )
    }
}