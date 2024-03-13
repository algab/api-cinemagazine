package br.com.cinemagazine.services.impl

import br.com.cinemagazine.constants.ApiMessage.WATCHED_NOT_FOUND
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.WatchedDocument
import br.com.cinemagazine.dto.watched.WatchedDTO
import br.com.cinemagazine.dto.watched.WatchedProductionDTO
import br.com.cinemagazine.dto.watched.WatchedRequestDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.repository.WatchedRepository
import br.com.cinemagazine.services.WatchedService
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class WatchedServiceImpl(
    private val watchedRepository: WatchedRepository,
    private val productionRepository: ProductionRepository
): WatchedService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun addWatchedProduction(data: WatchedRequestDTO): WatchedDTO {
        val media = Media.valueOf(data.media)
        val production = this.productionRepository.findByTmdbAndMedia(data.tmdbId, media)
        val watchedProduction = WatchedProductionDTO(
            production!!.production.id,
            production.production.title,
            production.production.originalTitle,
            production.production.poster
        )
        val document = this.watchedRepository.save(WatchedDocument(
            ObjectId().toString(),
            data.tmdbId,
            media,
            data.userId,
            LocalDate.parse(data.date),
            watchedProduction,
            LocalDateTime.now()
        ))
        logger.info("WatchedServiceImpl.addWatchedProduction - Successful Operation - data: [{}]", document)
        return WatchedDTO(document.id, document.date, document.production)
    }

    override fun getWatchedProductions(userId: String, pageable: Pageable): Page<WatchedDTO> {
        val documents = this.watchedRepository.findByUserId(userId, pageable)
        val result = documents.content.map { WatchedDTO(it.id, it.date, it.production) }
        return PageImpl(result)
    }

    override fun deleteWatchedProduction(id: String) {
        val document = this.watchedRepository.findById(id).orElseThrow {
            logger.error("WatchedServiceImpl.deleteWatchedProduction - {} - id: [{}]", WATCHED_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, WATCHED_NOT_FOUND)
        }
        this.watchedRepository.delete(document)
    }
}