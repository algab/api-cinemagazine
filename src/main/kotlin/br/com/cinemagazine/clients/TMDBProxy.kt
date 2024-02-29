package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.ProductionTMDB
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TMDBProxy(
    private val client: TMDBClient,
    @Value("\${tmdb.key}") private val apiKey: String,
    @Value("\${tmdb.language}") private val language: String
) {
    fun getTrending(): List<ProductionTMDB> {
        val trending = mutableListOf<ProductionTMDB>()
        val pageOneResults = client.trending("day", apiKey, language, 1)
        val pageTwoResults = client.trending("day", apiKey, language, 2)
        trending.addAll(pageOneResults.results)
        trending.addAll(pageTwoResults.results)
        return trending.filter{ it.media == "movie" || it.media == "tv" }
    }

    fun searchProduction(name: String): List<ProductionTMDB> {
        val resultSearch = mutableListOf<ProductionTMDB>()
        val resultSearchMovie = client.searchMovie(apiKey, language, name)
        val resultSearchTV = client.searchTV(apiKey, language, name)
        resultSearch.addAll(resultSearchMovie.results.map { mountProductionTMDB(it, "movie") })
        resultSearch.addAll(resultSearchTV.results.map { mountProductionTMDB(it, "tv") })
        return resultSearch.sortedByDescending { it.popularity }
    }

    private fun mountProductionTMDB(production: ProductionTMDB, type: String): ProductionTMDB {
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