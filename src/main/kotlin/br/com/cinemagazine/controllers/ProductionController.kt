package br.com.cinemagazine.controllers

import br.com.cinemagazine.annotation.authorization.Authorize
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.dto.production.SearchDTO
import br.com.cinemagazine.dto.production.TrendingDTO
import br.com.cinemagazine.services.ProductionService
import br.com.cinemagazine.services.SearchService
import br.com.cinemagazine.services.TrendingService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/productions")
class ProductionController(
    private val trendingService: TrendingService,
    private val searchService: SearchService,
    private val productionService: ProductionService
) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Authorize
    @GetMapping("/trending")
    fun getTrending(): ResponseEntity<List<TrendingDTO>> {
        val begin = System.currentTimeMillis()
        logger.debug("ProductionController.getTrending - Start")
        val trending = trendingService.getTrending()
        logger.debug("ProductionController.getTrending - End - Time: {} ms", System.currentTimeMillis() - begin)
        return ResponseEntity.ok(trending)
    }

    @Authorize
    @GetMapping("/search")
    fun search(@RequestParam("name") name: String): ResponseEntity<List<SearchDTO>> {
        val begin = System.currentTimeMillis()
        logger.debug("ProductionController.search - Start - Input: name [{}]", name)
        val results = searchService.search(name)
        logger.debug("ProductionController.search - End - Input: name [{}] - Time: {} ms", name, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(results)
    }

    @Authorize
    @GetMapping("/{id}")
    fun getProduction(@PathVariable id: Long, @RequestParam media: Media): ResponseEntity<ProductionDTO> {
        val begin = System.currentTimeMillis()
        logger.debug("ProductionController.getProduction - Start - Input: id [{}], media [{}]", id, media)
        val production = productionService.getProduction(id, media)
        logger.debug("ProductionController.getProduction - Start - Input: id [{}], media [{}] - Output: [{}] - Time: {} ms",
            id, media, production, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(production)
    }
}