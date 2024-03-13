package br.com.cinemagazine.dto.watched

import java.time.LocalDate

data class WatchedDTO(
    val id: String,
    val date: LocalDate,
    val production: WatchedProductionDTO
)