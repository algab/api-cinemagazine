package br.com.cinemagazine.dto.production

import br.com.cinemagazine.dto.tmdb.SeasonTMDB
import br.com.cinemagazine.serializer.ImageSerializer
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class TvDTO(
    override val id: Long,
    override val title: String,
    override val originalTitle: String,
    override val description: String,
    @JsonSerialize(using = ImageSerializer::class) override val poster: String,
    override val dateRelease: String,
    override val companies: List<String>,
    val seasons: List<SeasonTMDB>,
    val cast: List<CastTvDTO>,
    val crew: List<CrewTvDTO>
): ProductionDTO
