package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.CastTvTMDB
import br.com.cinemagazine.dto.tmdb.CreditTvTMDB
import br.com.cinemagazine.dto.tmdb.CrewTvTMDB

fun getCreditTvTMDB(crew: List<CrewTvTMDB> = listOf(getCrewTvTMDB()), sizeCast: Int = 1): CreditTvTMDB {
    val cast = mutableListOf<CastTvTMDB>()
    for (i in 1.rangeTo(sizeCast)) {
        cast.add(getCastTvTMDB())
    }
    return CreditTvTMDB(
        cast,
        crew
    )
}