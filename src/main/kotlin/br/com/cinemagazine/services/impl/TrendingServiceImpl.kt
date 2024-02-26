package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.trending.TrendingDTO
import br.com.cinemagazine.services.TrendingService
import org.springframework.stereotype.Service

@Service
class TrendingServiceImpl(private val proxy: TMDBProxy): TrendingService {
    override fun getTrending(): List<TrendingDTO> {
        val trending = proxy.getTrending().results
        return trending.map {
            TrendingDTO(it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media)
        }
    }
}