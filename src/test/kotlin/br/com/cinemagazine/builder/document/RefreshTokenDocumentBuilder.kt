package br.com.cinemagazine.builder.document

import br.com.cinemagazine.documents.RefreshTokenDocument
import java.time.LocalDateTime

fun getRefreshTokenDocument(): RefreshTokenDocument {
    return RefreshTokenDocument("1", "refresh-token", "agent", LocalDateTime.now())
}