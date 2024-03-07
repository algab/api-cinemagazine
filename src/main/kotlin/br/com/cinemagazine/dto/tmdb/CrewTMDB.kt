package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class CrewTMDB(
    val name: String,
    val job: String,
    @JsonAlias("profile_path")
    val image: String
)
