package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TMDBProxy(
    private val client: TMDBClient,
    @Value("\${tmdb.key}") private val apiKey: String,
    @Value("\${tmdb.language}") private val language: String
) {
    fun getTrending() = runBlocking { makeTrending() }

    suspend fun makeTrending(): List<TrendingTMDB> {
        val trending = mutableListOf<TrendingTMDB>()
        CoroutineScope(Dispatchers.IO).async {
            val pageOneResults = async { client.trending("day", apiKey, language, 1) }
            val pageTwoResults = async { client.trending("day", apiKey, language, 2) }
            trending.addAll(pageOneResults.await().results)
            trending.addAll(pageTwoResults.await().results)
        }.await()
        return trending.filter{ it.media == "movie" || it.media == "tv" }
    }
}