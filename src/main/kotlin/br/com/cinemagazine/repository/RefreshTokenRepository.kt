package br.com.cinemagazine.repository

import br.com.cinemagazine.documents.RefreshTokenDocument
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.Optional

interface RefreshTokenRepository: MongoRepository<RefreshTokenDocument, String> {
    fun findByToken(token: String): Optional<RefreshTokenDocument>
}