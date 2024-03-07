package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.production.TrendingDTO
import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import br.com.cinemagazine.services.TrendingService
import org.springframework.stereotype.Service

@Service
class TrendingServiceImpl(private val proxy: TMDBProxy): TrendingService {
    override fun getTrending(): List<TrendingDTO> {
        val trending = mutableListOf<TrendingTMDB>()
        val pageOneResults = proxy.trending(1)
        val pageTwoResults = proxy.trending(2)
        trending.addAll(pageOneResults.results)
        trending.addAll(pageTwoResults.results)
        return trending
            .filter { it.media == "movie" || it.media == "tv" }
            .map {
            TrendingDTO(it.id, it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media)
        }
    }
}