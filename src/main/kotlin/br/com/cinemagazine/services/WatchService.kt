package br.com.cinemagazine.services

import br.com.cinemagazine.dto.watch.WatchDTO
import br.com.cinemagazine.dto.watch.WatchRequestDTO
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface WatchService {
    fun addWatchProduction(data: WatchRequestDTO): WatchDTO
    fun getWatchProductions(userId: String, page: Pageable): Page<WatchDTO>
    fun deleteWatchProduction(id: String)
}