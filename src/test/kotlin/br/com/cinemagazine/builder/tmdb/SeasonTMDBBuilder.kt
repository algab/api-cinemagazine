package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.SeasonTMDB

fun getSeasonTMDB(): SeasonTMDB {
    return SeasonTMDB(
        1,
        "Season 1",
        1,
        20,
        "2024-03-12"
    )
}