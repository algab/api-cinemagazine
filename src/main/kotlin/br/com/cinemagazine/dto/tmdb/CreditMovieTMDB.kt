package br.com.cinemagazine.dto.tmdb

data class CreditMovieTMDB(
    val cast: List<CastMovieTMDB>,
    val crew: List<CrewMovieTMDB>
)