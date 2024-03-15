package br.com.cinemagazine.builder.watch

import br.com.cinemagazine.dto.watch.WatchProductionDTO

fun getWatchProductionDTO(): WatchProductionDTO {
    return WatchProductionDTO(
        10,
        "Test",
        "Test",
        "/test.png"
    )
}