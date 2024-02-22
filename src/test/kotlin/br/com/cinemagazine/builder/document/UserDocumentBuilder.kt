package br.com.cinemagazine.builder.document

import br.com.cinemagazine.constants.Gender
import br.com.cinemagazine.documents.UserDocument
import java.time.LocalDateTime

fun getUserDocument(): UserDocument {
    return UserDocument(
        "1",
        "Test",
        "Test",
        "test@email.com",
        "1234",
        Gender.MASCULINE,
        LocalDateTime.now(),
        LocalDateTime.now()
    )
}