package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.TvTMDB

fun getTvTMDB(): TvTMDB {
    return TvTMDB(
        1,
        "Test",
        "test",
        "test",
        "http://image.com/test.png",
        "2024-03-12",
        10,
        listOf(getCompanyTMDB()),
        listOf(getSeasonTMDB())
    )
}