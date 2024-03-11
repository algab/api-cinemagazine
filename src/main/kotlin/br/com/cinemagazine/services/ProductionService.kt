package br.com.cinemagazine.services

import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.ProductionDTO

interface ProductionService {
    fun getProduction(id: Long, media: Media): ProductionDTO
}