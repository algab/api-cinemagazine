package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.PageTMDB
import br.com.cinemagazine.dto.tmdb.ProductionTMDB
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TMDBProxy(
    private val client: TMDBClient,
    @Value("\${tmdb.key}") private val apiKey: String,
    @Value("\${tmdb.language}") private val language: String
) {
    fun trending(page: Int): PageTMDB<ProductionTMDB> {
        return client.trending("day", apiKey, language, page)
    }

    fun searchMovie(name: String): PageTMDB<ProductionTMDB> {
        return client.searchMovie(apiKey, language, name)
    }

    fun searchTV(name: String): PageTMDB<ProductionTMDB> {
        return client.searchTV(apiKey, language, name)
    }
}