package br.com.cinemagazine.dto.watched

data class WatchedProductionDTO(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val poster: String
)