package br.com.cinemagazine.services

import br.com.cinemagazine.dto.production.ProductionDTO

interface TrendingService {
    fun getTrending(): List<ProductionDTO>
}