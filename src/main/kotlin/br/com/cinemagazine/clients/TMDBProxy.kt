package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TMDBProxy(
    private val client: TMDBClient,
    @Value("\${tmdb.key}") private val apiKey: String,
    @Value("\${tmdb.language}") private val language: String
) {
    fun getTrending(): List<TrendingTMDB> {
        val trending = mutableListOf<TrendingTMDB>()
        val pageOneResults = client.trending("day", apiKey, language, 1)
        val pageTwoResults = client.trending("day", apiKey, language, 2)
        trending.addAll(pageOneResults.results)
        trending.addAll(pageTwoResults.results)
        return trending.filter{ it.media == "movie" || it.media == "tv" }
    }
}