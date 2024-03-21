package br.com.cinemagazine.documents

import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.watched.WatchedProductionDTO
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

@Document(collection = "watched")
data class WatchedDocument(
    @Id val id: String,
    val tmdbId: Long,
    val media: Media,
    val userId: String,
    val rating: Int,
    val date: LocalDate,
    val production: WatchedProductionDTO,
    val createdDate: LocalDateTime
)