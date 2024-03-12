package br.com.cinemagazine.builder.production

import br.com.cinemagazine.dto.production.MovieDTO

fun getMovieDTO(): MovieDTO {
    return MovieDTO(
        1,
        "Test",
        "test",
        "test",
        "http://image.com/test.png",
        "2024-03-12",
        listOf("Test"),
        listOf(getCastMovieDTO()),
        listOf(getCrewMovieDTO())
    )
}