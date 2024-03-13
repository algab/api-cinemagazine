package br.com.cinemagazine.services

import br.com.cinemagazine.dto.watched.WatchedDTO
import br.com.cinemagazine.dto.watched.WatchedRequestDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface WatchedService {
    fun addWatchedProduction(data: WatchedRequestDTO): WatchedDTO
    fun getWatchedProductions(userId: String, pageable: Pageable): Page<WatchedDTO>
    fun deleteWatchedProduction(id: String)
}