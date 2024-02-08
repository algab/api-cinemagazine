package br.com.cinemagazine.repository

import br.com.cinemagazine.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository
import java.util.*

interface UserRepository: MongoRepository<UserDocument, String> {
    fun findByEmail(email: String): Optional<UserDocument>
    fun existsByEmail(email: String): Boolean
}