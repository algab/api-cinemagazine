package br.com.cinemagazine.dto.tmdb

data class CreditTMDB(
    val cast: List<CastTMDB>,
    val crew: List<CrewTMDB>
)