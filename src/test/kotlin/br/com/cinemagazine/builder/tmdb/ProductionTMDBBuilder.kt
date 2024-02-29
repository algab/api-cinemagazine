package br.com.cinemagazine.builder.tmdb

import br.com.cinemagazine.dto.tmdb.ProductionTMDB

fun getProductionTMDB(title: String = "Test", media: String = "movie"): ProductionTMDB {
    return ProductionTMDB(
        1,
        title,
        "Test",
        "Test",
        "/test.png",
        "2024-02-29",
        media,
        100
    )
}