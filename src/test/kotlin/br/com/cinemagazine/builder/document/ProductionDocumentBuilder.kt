package br.com.cinemagazine.builder.document

import br.com.cinemagazine.builder.production.getMovieDTO
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.ProductionDocument

fun getProductionDocument(): ProductionDocument {
    return ProductionDocument(
        "1",
        1,
        Media.MOVIE,
        getMovieDTO()
    )
}