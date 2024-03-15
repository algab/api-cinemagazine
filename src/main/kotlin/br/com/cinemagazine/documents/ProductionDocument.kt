package br.com.cinemagazine.documents

import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.ProductionDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "production")
data class ProductionDocument(
    @Id var id: String,
    var tmdbId: Long,
    var media: Media,
    var production: ProductionDTO
)
