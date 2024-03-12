package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.MovieTMDB

fun getMovieTMDB(): MovieTMDB {
    return MovieTMDB(
        1,
        "Test",
        "test",
        "test",
        "http://image.com/test.png",
        "2024-03-12",
        10,
        listOf(getCompanyTMDB())
    )
}