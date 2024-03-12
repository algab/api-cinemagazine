package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.CreditMovieTMDB
import br.com.cinemagazine.dto.tmdb.CrewMovieTMDB

fun getCreditMovieTMDB(crew: List<CrewMovieTMDB> = listOf(getCrewMovieTMDB())): CreditMovieTMDB {
    return CreditMovieTMDB(
        listOf(getCastMovieTMDB()),
        crew
    )
}