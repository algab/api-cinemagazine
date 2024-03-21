package br.com.cinemagazine.builder.watched

import br.com.cinemagazine.dto.watched.WatchedDTO
import java.time.LocalDate

fun getWatchedDTO(): WatchedDTO {
    return WatchedDTO(
        "1",
        5,
        LocalDate.now(),
        getWatchedProductionDTO()
    )
}