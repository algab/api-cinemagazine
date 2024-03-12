package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.CrewMovieTMDB

fun getCrewMovieTMDB(job: String = "Director"): CrewMovieTMDB {
    return CrewMovieTMDB(
        "Test",
        job,
        "http://image.com/director.png"
    )
}