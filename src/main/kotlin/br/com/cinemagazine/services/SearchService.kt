package br.com.cinemagazine.services

import br.com.cinemagazine.dto.production.ProductionDTO

interface SearchService {
    fun search(name: String): List<ProductionDTO>
}