package br.com.cinemagazine.dto.trending

import br.com.cinemagazine.serialize.ImageSerialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class TrendingDTO(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val description: String,
    @JsonSerialize(using = ImageSerialize::class)
    val poster: String,
    val dateRelease: String,
    val media: String
)
