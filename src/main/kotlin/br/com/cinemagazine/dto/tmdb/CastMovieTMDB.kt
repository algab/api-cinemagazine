package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class CastMovieTMDB(
    val name: String,
    val character: String,
    @JsonAlias("profile_path")
    val image: String?
)