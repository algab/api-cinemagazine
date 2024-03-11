package br.com.cinemagazine.dto.tmdb

data class CreditTvTMDB(
    val cast: List<CastTvTMDB>,
    val crew: List<CrewTvTMDB>
)