package br.com.cinemagazine.dto.watch

import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class WatchProductionDTO(
    val id: Long,
    val title: String,
    val originalTitle: String,
    @JsonSerialize(using = ImageSerializer::class) val poster: String
)
