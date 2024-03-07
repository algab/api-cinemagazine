package br.com.cinemagazine.services

import br.com.cinemagazine.dto.production.TrendingDTO

interface TrendingService {
    fun getTrending(): List<TrendingDTO>
}