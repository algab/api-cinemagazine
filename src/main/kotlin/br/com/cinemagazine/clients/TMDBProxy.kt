package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import br.com.cinemagazine.dto.tmdb.PageTMDB
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TMDBProxy(
    private val client: TMDBClient,
    @Value("\${tmdb.key}") private val apiKey: String,
    @Value("\${tmdb.language}") private val language: String
) {
    fun getTrending(): PageTMDB<TrendingTMDB> {
        return this.client.trending("day", apiKey, language)
    }
}