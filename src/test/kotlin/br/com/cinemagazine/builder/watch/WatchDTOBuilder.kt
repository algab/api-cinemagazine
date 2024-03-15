package br.com.cinemagazine.builder.watch

import br.com.cinemagazine.dto.watch.WatchDTO

fun getWatchDTO(): WatchDTO {
    return WatchDTO(
        "1",
        getWatchProductionDTO()
    )
}