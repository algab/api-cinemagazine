package br.com.cinemagazine.controllers

import br.com.cinemagazine.dto.trending.TrendingDTO
import br.com.cinemagazine.services.TrendingService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/trending")
class TrendingController(private val trendingService: TrendingService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping
    fun getTrending(): ResponseEntity<List<TrendingDTO>> {
        val begin = System.currentTimeMillis()
        logger.debug("TrendingController.getTrending - Start")
        val trending = trendingService.getTrending()
        logger.debug("TrendingController.getTrending - End - Time: {} ms", System.currentTimeMillis() - begin)
        return ResponseEntity.ok(trending)
    }
}