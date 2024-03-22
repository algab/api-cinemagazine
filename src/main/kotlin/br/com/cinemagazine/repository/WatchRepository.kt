package br.com.cinemagazine.repository

import br.com.cinemagazine.documents.WatchDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository

interface WatchRepository: MongoRepository<WatchDocument, String> {
    fun countByTmdbIdAndUserId(tmdbId: Long, userId: String): Long
    fun countByUserId(userId: String): Long
    fun findByUserId(userId: String, page: Pageable): Page<WatchDocument>
}