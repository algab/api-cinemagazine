package br.com.cinemagazine.dto.watched

import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class WatchedProductionDTO(
    val id: Long,
    val title: String,
    val originalTitle: String,
    @JsonSerialize(using = ImageSerializer::class) val poster: String
)