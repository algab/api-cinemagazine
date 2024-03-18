package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.TrendingDTO
import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import br.com.cinemagazine.services.TrendingService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Service

@Service
class TrendingServiceImpl(private val proxy: TMDBProxy): TrendingService {
    override fun getTrending() = runBlocking {
        val trending = mutableListOf<TrendingTMDB>()
        val deferredTrending = awaitAll(*listOf(
            async { proxy.trending(1) },
            async { proxy.trending(2) }
        ).toTypedArray())
        trending.addAll(deferredTrending[0].results)
        trending.addAll(deferredTrending[1].results)
        trending
            .filter { it.media == Media.MOVIE.value || it.media == Media.TV.value }
            .map {
                TrendingDTO(it.id, it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media)
            }
    }
}