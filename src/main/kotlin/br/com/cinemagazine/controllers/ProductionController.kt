package br.com.cinemagazine.controllers

import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.services.ProductionService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/productions")
class ProductionController(private val service: ProductionService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping("/{id}")
    fun getProduction(@PathVariable id: Long, @RequestParam media: Media): ResponseEntity<ProductionDTO> {
        val begin = System.currentTimeMillis()
        logger.debug("ProductionController.getProduction - Start - Input: id [{}], media [{}]", id, media)
        val result = service.getProduction(id, media)
        logger.debug("ProductionController.getProduction - Start - Input: id [{}], media [{}] - Time: {} ms",
            id, media, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(result)
    }
}