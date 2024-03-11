package br.com.cinemagazine.repository

import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.ProductionDocument
import org.springframework.data.mongodb.repository.MongoRepository

interface ProductionRepository: MongoRepository<ProductionDocument, String> {
    fun findByTmdbAndMedia(tmdb: Long, media: Media): ProductionDocument?
}