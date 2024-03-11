package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class SeasonTMDB(
    val id: Long,
    val name: String,
    @JsonAlias("season_number")
    val season: Int,
    @JsonAlias("episode_count")
    val totalEpisodes: Int,
    @JsonAlias("air_date")
    val dateRelease: String?
)
