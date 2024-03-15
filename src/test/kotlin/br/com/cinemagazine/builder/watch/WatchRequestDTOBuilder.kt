package br.com.cinemagazine.builder.watch

import br.com.cinemagazine.dto.watch.WatchRequestDTO

fun getWatchRequestDTO(): WatchRequestDTO {
    return WatchRequestDTO(
        1010,
        "movie",
        "10"
    )
}