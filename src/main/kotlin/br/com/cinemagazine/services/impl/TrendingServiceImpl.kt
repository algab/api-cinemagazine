package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.services.TrendingService
import org.springframework.stereotype.Service

@Service
class TrendingServiceImpl(private val proxy: TMDBProxy): TrendingService {
    override fun getTrending(): List<ProductionDTO> {
        val trending = proxy.getTrending()
        return trending.map {
            ProductionDTO(it.id, it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media!!)
        }
    }
}