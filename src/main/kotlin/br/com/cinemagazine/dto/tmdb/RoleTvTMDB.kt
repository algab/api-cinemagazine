package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class RoleTvTMDB(
    val character: String,
    @JsonAlias("episode_count")
    val totalEpisodes: Int
)