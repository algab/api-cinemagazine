package br.com.cinemagazine.services.impl

import br.com.cinemagazine.constants.ApiMessage.PRODUCTION_NOT_FOUND
import br.com.cinemagazine.constants.ApiMessage.WATCH_EXISTS
import br.com.cinemagazine.constants.ApiMessage.WATCH_NOT_FOUND
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.WatchDocument
import br.com.cinemagazine.dto.watch.WatchDTO
import br.com.cinemagazine.dto.watch.WatchProductionDTO
import br.com.cinemagazine.dto.watch.WatchRequestDTO
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.repository.WatchRepository
import br.com.cinemagazine.services.WatchService
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus.CONFLICT
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class WatchServiceImpl(
    private val watchRepository: WatchRepository,
    private val productionRepository: ProductionRepository
): WatchService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun addWatchProduction(data: WatchRequestDTO): WatchDTO {
        val media = Media.getMedia(data.media!!)
        val production = this.productionRepository.findByTmdbIdAndMedia(data.tmdbId!!, media!!)
        if (Objects.isNull(production)) {
            logger.error("WatchServiceImpl.addWatchProduction - {} - tmdbId: [{}]",
                PRODUCTION_NOT_FOUND.description, data.tmdbId)
            throw BusinessException(NOT_FOUND, PRODUCTION_NOT_FOUND)
        }
        val countDocuments = this.watchRepository.countByTmdbIdAndUserId(data.tmdbId, data.userId!!)
        if (countDocuments > 0) {
            logger.error("WatchServiceImpl.addWatchProduction - {} - tmdbId: [{}], userId: [{}]",
                WATCH_EXISTS.description, data.tmdbId, data.userId)
            throw BusinessException(CONFLICT, WATCH_EXISTS)
        }
        val watchProduction = WatchProductionDTO(
            production!!.production.id,
            production.production.title,
            production.production.originalTitle,
            production.production.poster
        )
        val document = watchRepository.save(WatchDocument(
            ObjectId().toString(),
            data.tmdbId,
            media,
            data.userId,
            watchProduction,
            LocalDateTime.now()
        ))
        logger.info("WatchServiceImpl.addWatchProduction - Successful Operation - data: [{}]", document)
        return WatchDTO(document.id, document.production)
    }

    override fun getWatchProductions(userId: String, page: Pageable): Page<WatchDTO> {
        val totalDocuments = this.watchRepository.countByUserId(userId)
        val documents = this.watchRepository.findByUserId(userId, page).content.map {
            WatchDTO(it.id, it.production)
        }
        return PageImpl(documents, page, totalDocuments)
    }

    override fun deleteWatchProduction(id: String) {
        val document = this.watchRepository.findById(id).orElseThrow {
            logger.error("WatchServiceImpl.deleteWatchProduction - {} - id: [{}]", WATCH_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, WATCH_NOT_FOUND)
        }
        this.watchRepository.delete(document)
    }
}