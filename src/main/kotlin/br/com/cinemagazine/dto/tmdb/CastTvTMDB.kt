package br.com.cinemagazine.dto.tmdb

import com.fasterxml.jackson.annotation.JsonAlias

data class CastTvTMDB(
    val name: String,
    @JsonAlias("profile_path")
    val image: String?,
    val roles: List<RoleTvTMDB>
)
