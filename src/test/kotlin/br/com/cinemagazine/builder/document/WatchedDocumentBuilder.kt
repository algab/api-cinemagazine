package br.com.cinemagazine.builder.document

import br.com.cinemagazine.builder.watched.getWatchedProductionDTO
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.WatchedDocument
import java.time.LocalDate
import java.time.LocalDateTime

fun getWatchedDocument(): WatchedDocument {
    return WatchedDocument(
        "1",
        10,
        Media.TV,
        "1010",
        LocalDate.now(),
        getWatchedProductionDTO(),
        LocalDateTime.now()
    )
}