package br.com.cinemagazine.builder.production

import br.com.cinemagazine.dto.production.ProductionDTO

fun getProductionDTO(): ProductionDTO {
    return ProductionDTO(
        1,
        "Test",
        "Test",
        "Test",
        "https://image.tmdb.org/t/p/original/test.png",
        "2024-02-29",
        "movie"
    )
}