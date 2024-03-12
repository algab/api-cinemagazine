package br.com.cinemagazine.builder.production

import br.com.cinemagazine.dto.production.CastMovieDTO

fun getCastMovieDTO(): CastMovieDTO {
    return CastMovieDTO(
        "Test",
        "Actor",
        "http://image.com/actor.png"
    )
}