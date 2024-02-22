package br.com.cinemagazine.documents

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "refresh-token")
data class RefreshTokenDocument(
    @Id var id: String,
    var token: String,
    var agent: String,
    var createdDate: LocalDateTime
)
