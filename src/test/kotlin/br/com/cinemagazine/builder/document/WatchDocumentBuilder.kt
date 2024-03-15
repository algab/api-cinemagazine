package br.com.cinemagazine.builder.document

import br.com.cinemagazine.builder.watch.getWatchProductionDTO
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.WatchDocument
import java.time.LocalDateTime

fun getWatchDocument(): WatchDocument {
    return WatchDocument(
        "1",
        10,
        Media.MOVIE,
        "1010",
        getWatchProductionDTO(),
        LocalDateTime.now()
    )
}