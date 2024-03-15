package br.com.cinemagazine.builder.watched

import br.com.cinemagazine.dto.watched.WatchedProductionDTO

fun getWatchedProductionDTO(): WatchedProductionDTO {
    return WatchedProductionDTO(
        1,
        "Test",
        "Test",
        "/test.png"
    )
}