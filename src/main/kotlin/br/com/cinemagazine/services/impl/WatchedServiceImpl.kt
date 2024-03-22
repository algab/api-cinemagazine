package br.com.cinemagazine.services.impl

import br.com.cinemagazine.constants.ApiMessage.PRODUCTION_NOT_FOUND
import br.com.cinemagazine.constants.ApiMessage.WATCHED_EXISTS
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
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.Objects

@Service
class WatchedServiceImpl(
    private val watchedRepository: WatchedRepository,
    private val productionRepository: ProductionRepository
): WatchedService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun addWatchedProduction(data: WatchedRequestDTO): WatchedDTO {
        val media = Media.getMedia(data.media!!)
        val production = this.productionRepository.findByTmdbIdAndMedia(data.tmdbId!!, media!!)
        if (Objects.isNull(production)) {
            logger.error("WatchedServiceImpl.addWatchedProduction - {} - tmdbId: [{}]",
                PRODUCTION_NOT_FOUND.description, data.tmdbId)
            throw BusinessException(NOT_FOUND, PRODUCTION_NOT_FOUND)
        }
        val countDocuments = this.watchedRepository.countByTmdbIdAndUserId(data.tmdbId, data.userId!!)
        if (countDocuments > 0) {
            logger.error("WatchedServiceImpl.addWatchedProduction - {} - tmdbId: [{}], userId: [{}]",
                WATCHED_EXISTS.description, data.tmdbId, data.userId)
            throw BusinessException(CONFLICT, WATCHED_EXISTS)
        }
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
            data.rating!!,
            LocalDate.parse(data.date),
            watchedProduction,
            LocalDateTime.now()
        ))
        logger.info("WatchedServiceImpl.addWatchedProduction - Successful Operation - data: [{}]", document)
        return WatchedDTO(document.id, document.rating, document.date, document.production)
    }

    override fun getWatchedProductions(userId: String, page: Pageable): Page<WatchedDTO> {
        val totalDocuments = this.watchedRepository.countByUserId(userId)
        val documents = this.watchedRepository.findByUserId(userId, page).content.map {
            WatchedDTO(it.id, it.rating, it.date, it.production)
        }
        return PageImpl(documents, page, totalDocuments)
    }

    override fun deleteWatchedProduction(id: String) {
        val document = this.watchedRepository.findById(id).orElseThrow {
            logger.error("WatchedServiceImpl.deleteWatchedProduction - {} - id: [{}]", WATCHED_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, WATCHED_NOT_FOUND)
        }
        this.watchedRepository.delete(document)
    }
}