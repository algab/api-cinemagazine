package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class TvTMDB(
    override val id: Long,
    @JsonAlias("title", "name")
    override val title: String,
    @JsonAlias("original_title", "original_name")
    override val originalTitle: String,
    @JsonAlias("overview")
    override val description: String,
    @JsonAlias("poster_path")
    override val poster: String?,
    @JsonAlias("release_date", "first_air_date")
    override val dateRelease: String,
    @JsonAlias("media_type")
    override val media: String?,
    override val popularity: Long,
    @JsonAlias("production_companies")
    val companies: List<CompanyTMDB>,
    val seasons: List<SeasonTMDB>
): ProductionTMDB
