package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class TvTMDB(
    val id: Long,
    @JsonAlias("title", "name")
    val title: String,
    @JsonAlias("original_title", "original_name")
    val originalTitle: String,
    @JsonAlias("overview")
    val description: String,
    @JsonAlias("poster_path")
    val poster: String?,
    @JsonAlias("release_date", "first_air_date")
    val dateRelease: String,
    val popularity: Long,
    @JsonAlias("production_companies")
    val companies: List<CompanyTMDB>,
    val seasons: List<SeasonTMDB>
)
