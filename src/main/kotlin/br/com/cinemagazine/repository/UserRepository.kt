package br.com.cinemagazine.repository

import br.com.cinemagazine.document.UserDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface UserRepository: MongoRepository<UserDocument, String> {
    fun existsByEmail(email: String): Boolean
}