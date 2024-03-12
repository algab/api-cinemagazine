package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.CastMovieTMDB

fun getCastMovieTMDB(): CastMovieTMDB {
    return CastMovieTMDB(
        "Test",
        "Actor",
        "http://image.com/actor.png"
    )
}