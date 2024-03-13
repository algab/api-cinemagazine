package br.com.cinemagazine.documents

import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.watch.WatchProductionDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "watch")
data class WatchDocument(
    @Id val id: String,
    val tmdbId: Long,
    val media: Media,
    val userId: String,
    val production: WatchProductionDTO,
    val createdDate: LocalDateTime
)
