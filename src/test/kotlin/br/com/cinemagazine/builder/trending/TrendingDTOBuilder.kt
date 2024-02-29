package br.com.cinemagazine.builder.trending

import br.com.cinemagazine.dto.trending.TrendingDTO

fun getTrendingDTO(): TrendingDTO {
    return TrendingDTO(
        1,
        "Test",
        "Test",
        "Test",
        "https://image.tmdb.org/t/p/original/test.png",
        "2024-02-29",
        "movie"
    )
}