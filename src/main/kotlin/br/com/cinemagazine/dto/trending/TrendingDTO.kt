package br.com.cinemagazine.dto.trending

data class TrendingDTO(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val description: String,
    val poster: String,
    val dateRelease: String,
    val media: String
)
