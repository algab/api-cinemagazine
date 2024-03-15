package br.com.cinemagazine.controllers

import br.com.cinemagazine.dto.watch.WatchDTO
import br.com.cinemagazine.dto.watch.WatchRequestDTO
import br.com.cinemagazine.services.WatchService
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
@RequestMapping("/v1/watch")
class WatchController(private val service: WatchService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @PostMapping
    fun addWatchProduction(@Valid @RequestBody body: WatchRequestDTO): ResponseEntity<WatchDTO> {
        val begin = System.currentTimeMillis()
        logger.info("WatchController.addWatchProduction - Start - Input: body [{}]", body)
        val result = service.addWatchProduction(body)
        logger.info("WatchController.addWatchProduction - End - Input: body [{}] - Output: [{}] - Time: {} ms",
            body, result, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getWatchProductions(
        @RequestParam userId: String,
        @PageableDefault page: Pageable
    ): ResponseEntity<Page<WatchDTO>> {
        val begin = System.currentTimeMillis()
        logger.debug("WatchController.getWatchProductions - Start - Input: userId [{}]", userId)
        val result = service.getWatchProductions(userId, page)
        logger.debug("WatchController.getWatchProductions - End - Input: userId [{}] - Output: [{}] - Time: {} ms",
            userId, result.content, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(result)
    }

    @DeleteMapping("/{id}")
    fun deleteWatchProduction(@PathVariable id: String): ResponseEntity<Unit> {
        val begin = System.currentTimeMillis()
        logger.info("WatchController.deleteWatchProduction - Start - Input: id [{}]", id)
        service.deleteWatchProduction(id)
        logger.info("WatchController.deleteWatchProduction - End - Input: id [{}] - Time: {} ms",
            id, System.currentTimeMillis() - begin)
        return ResponseEntity.noContent().build()
    }

}