package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.services.SearchService
import org.springframework.stereotype.Service

@Service
class SearchServiceImpl(private val proxy: TMDBProxy): SearchService {
    override fun search(name: String): List<ProductionDTO> {
        val results = proxy.searchProduction(name)
        return results.map {
            ProductionDTO(it.id, it.title, it.originalTitle, it.description, it.poster, it.dateRelease, it.media!!)
        }
    }
}