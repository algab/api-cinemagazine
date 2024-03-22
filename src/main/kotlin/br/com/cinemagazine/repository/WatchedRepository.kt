package br.com.cinemagazine.repository

import br.com.cinemagazine.documents.WatchedDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface WatchedRepository: MongoRepository<WatchedDocument, String> {
    fun countByTmdbIdAndUserId(tmdbId: Long, userId: String): Long
    fun countByUserId(userId: String): Long
    fun findByUserId(userId: String, page: Pageable): Page<WatchedDocument>
}