package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.production.SearchDTO
import br.com.cinemagazine.dto.tmdb.SearchTMDB
import br.com.cinemagazine.services.SearchService
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(private val proxy: TMDBProxy): SearchService {
    override fun search(name: String): List<SearchDTO> {
        val resultSearch = mutableListOf<SearchDTO>()
        val resultSearchMovie = proxy.searchMovie(name)
        val resultSearchTV = proxy.searchTV(name)
        resultSearch.addAll(resultSearchMovie.results.map { mountProduction(it, "movie") })
        resultSearch.addAll(resultSearchTV.results.map { mountProduction(it, "tv") })
        return resultSearch.sortedByDescending { it.popularity }
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