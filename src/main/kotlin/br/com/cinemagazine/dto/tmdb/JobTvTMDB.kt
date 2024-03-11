package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class JobTvTMDB(
    val job: String,
    @JsonAlias("episode_count")
    val totalEpisodes: Int
)
