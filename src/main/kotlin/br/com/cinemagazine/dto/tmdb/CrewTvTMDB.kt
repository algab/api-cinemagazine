package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class CrewTvTMDB(
    val name: String,
    @JsonAlias("profile_path")
    val image: String?,
    val jobs: List<JobTvTMDB>
)