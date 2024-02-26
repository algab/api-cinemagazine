package br.com.cinemagazine.services

import br.com.cinemagazine.dto.trending.TrendingDTO

interface TrendingService {
    fun getTrending(): List<TrendingDTO>
}