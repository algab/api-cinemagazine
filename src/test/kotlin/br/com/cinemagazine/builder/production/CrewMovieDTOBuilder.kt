package br.com.cinemagazine.builder.production

import br.com.cinemagazine.dto.production.CrewMovieDTO

fun getCrewMovieDTO(): CrewMovieDTO {
    return CrewMovieDTO(
        "Test",
        "Director",
        "http://image.com/director.png"
    )
}