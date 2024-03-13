package br.com.cinemagazine.services.impl

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
import org.springframework.http.HttpStatus.NOT_FOUND
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class WatchServiceImpl(
    private val watchRepository: WatchRepository,
    private val productionRepository: ProductionRepository
): WatchService {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    override fun addWatchProduction(data: WatchRequestDTO): WatchDTO {
        val media = Media.valueOf(data.media)
        val production = this.productionRepository.findByTmdbAndMedia(data.tmdbId, media)
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
        val documents = this.watchRepository.findByUserId(userId, page).content.map {
            WatchDTO(it.id, it.production)
        }
        return PageImpl(documents)
    }

    override fun deleteWatchProduction(id: String) {
        val document = this.watchRepository.findById(id).orElseThrow {
            logger.error("WatchServiceImpl.deleteWatchProduction - {} - id: [{}]", WATCH_NOT_FOUND.description, id)
            throw BusinessException(NOT_FOUND, WATCH_NOT_FOUND)
        }
        this.watchRepository.delete(document)
    }
}