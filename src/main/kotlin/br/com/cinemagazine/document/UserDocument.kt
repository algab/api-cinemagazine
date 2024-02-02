package br.com.cinemagazine.document

import br.com.cinemagazine.constants.Gender
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "user")
data class UserDocument(
    @Id var id: String,
    var firstName: String,
    var lastName: String,
    var email: String,
    var password: String,
    var gender: Gender,
    var createdDate: LocalDateTime,
    var updatedDate: LocalDateTime
)
