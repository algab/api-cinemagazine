package br.com.cinemagazine.builder.watched

import br.com.cinemagazine.dto.watched.WatchedRequestDTO
import java.time.LocalDate

fun getWatchedRequestDTO(): WatchedRequestDTO {
    return WatchedRequestDTO(
        1,
        "tv",
        "1010",
        LocalDate.now().toString()
    )
}