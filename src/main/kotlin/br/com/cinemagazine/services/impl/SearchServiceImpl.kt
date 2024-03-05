package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.dto.tmdb.ProductionTMDB
import br.com.cinemagazine.services.SearchService
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(private val proxy: TMDBProxy): SearchService {
    override fun search(name: String): List<ProductionDTO> {
        val resultSearch = mutableListOf<ProductionTMDB>()
        val resultSearchMovie = proxy.searchMovie(name)
        val resultSearchTV = proxy.searchTV(name)
        resultSearch.addAll(resultSearchMovie.results.map { mountProduction(it, "movie") })
        resultSearch.addAll(resultSearchTV.results.map { mountProduction(it, "tv") })
        return resultSearch
            .sortedByDescending { it.popularity }
            .map {
            ProductionDTO(it.id, it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media!!)
        }
    }

    private fun mountProduction(production: ProductionTMDB, type: String): ProductionTMDB {
        return ProductionTMDB(
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