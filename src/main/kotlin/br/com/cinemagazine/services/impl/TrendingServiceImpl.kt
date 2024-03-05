package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.dto.tmdb.ProductionTMDB
import br.com.cinemagazine.services.TrendingService
import org.springframework.stereotype.Service

@Service
class TrendingServiceImpl(private val proxy: TMDBProxy): TrendingService {
    override fun getTrending(): List<ProductionDTO> {
        val trending = mutableListOf<ProductionTMDB>()
        val pageOneResults = proxy.trending(1)
        val pageTwoResults = proxy.trending(2)
        trending.addAll(pageOneResults.results)
        trending.addAll(pageTwoResults.results)
        return trending
            .filter { it.media == "movie" || it.media == "tv" }
            .map {
            ProductionDTO(it.id, it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media!!)
        }
    }
}