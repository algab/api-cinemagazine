package br.com.cinemagazine.dto.tmdb

import br.com.cinemagazine.serialize.ImageSerialize
import com.fasterxml.jackson.annotation.JsonAlias
import com.fasterxml.jackson.databind.annotation.JsonSerialize

data class TrendingTMDB(
    val id: Long,
    @JsonAlias("title", "name")
    val title: String,
    @JsonAlias("original_title", "original_name")
    val originalTitle: String,
    @JsonAlias("overview")
    val description: String,
    @JsonAlias("poster_path")
    @JsonSerialize(using = ImageSerialize::class)
    val poster: String,
    @JsonAlias("release_date", "first_air_date")
    val dateRelease: String,
    @JsonAlias("media_type")
    val media: String
)
