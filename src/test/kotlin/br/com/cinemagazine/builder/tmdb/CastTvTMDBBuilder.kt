package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.CastTvTMDB
import br.com.cinemagazine.dto.tmdb.RoleTvTMDB

fun getCastTvTMDB(): CastTvTMDB {
    return CastTvTMDB(
        "Test",
        "http://image.com/actor.png",
        listOf(RoleTvTMDB("Actor", 20))
    )
}