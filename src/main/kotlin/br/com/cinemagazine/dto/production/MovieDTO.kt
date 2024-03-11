package br.com.cinemagazine.dto.production

import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class MovieDTO(
    override val id: Long,
    override val title: String,
    override val originalTitle: String,
    override val description: String,
    @JsonSerialize(using = ImageSerializer::class) override val poster: String,
    override val dateRelease: String,
    override val companies: List<String>,
    val cast: List<CastMovieDTO>,
    val crew: List<CrewMovieDTO>
): ProductionDTO