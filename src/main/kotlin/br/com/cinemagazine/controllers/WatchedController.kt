package br.com.cinemagazine.controllers

import br.com.cinemagazine.dto.watched.WatchedDTO
import br.com.cinemagazine.dto.watched.WatchedRequestDTO
import br.com.cinemagazine.services.WatchedService
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/watched")
class WatchedController(private val service: WatchedService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping
    fun addWatchedProduction(@Valid @RequestBody body: WatchedRequestDTO): ResponseEntity<WatchedDTO> {
        val begin = System.currentTimeMillis()
        logger.info("WatchedController.addWatchedProduction - Start - Input: body [{}]", body)
        val result = service.addWatchedProduction(body)
        logger.info("WatchedController.addWatchedProduction - End - Input: body [{}] - Output: [{}] - Time: {} ms",
            body, result, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getWatchedProductions(
        @RequestParam userId: String,
        @PageableDefault page: Pageable
    ): ResponseEntity<Page<WatchedDTO>> {
        val begin = System.currentTimeMillis()
        logger.info("WatchedController.getWatchedProductions - Start - Input: userId [{}]", userId)
        val result = service.getWatchedProductions(userId, page)
        logger.info("WatchedController.getWatchedProductions - End - Input: userId [{}] - Output: [{}] - Time: {} ms",
            userId, result, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun deleteWatchedProduction(@PathVariable id: String): ResponseEntity<Unit> {
        val begin = System.currentTimeMillis()
        logger.info("WatchedController.deleteWatchedProduction - Start - Input: id [{}]", id)
        service.deleteWatchedProduction(id)
        logger.info("WatchedController.deleteWatchedProduction - End - Input: id [{}] - Time: {} ms",
            id, System.currentTimeMillis() - begin)
        return ResponseEntity.noContent().build()
    }
}