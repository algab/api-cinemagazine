package br.com.cinemagazine.controllers

import br.com.cinemagazine.dto.production.SearchDTO
import br.com.cinemagazine.services.SearchService
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/search")
class SearchController(private val searchService: SearchService) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @GetMapping
    fun search(@RequestParam("name") name: String): ResponseEntity<List<SearchDTO>> {
        val begin = System.currentTimeMillis()
        logger.debug("SearchController.search - Start - Input: name [{}]", name)
        val results = searchService.search(name)
        logger.debug("SearchController.search - End - Input: name [{}] - Time: {} ms", name, System.currentTimeMillis() - begin)
        return ResponseEntity.ok(results)
    }
}