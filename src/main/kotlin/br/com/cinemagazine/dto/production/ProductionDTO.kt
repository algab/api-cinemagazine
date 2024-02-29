package br.com.cinemagazine.dto.production

import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class ProductionDTO(
    val id: Long,
    val title: String,
    val originalTitle: String,
    val description: String,
    @JsonSerialize(using = ImageSerializer::class)
    val poster: String?,
    val dateRelease: String,
    val media: String
)
