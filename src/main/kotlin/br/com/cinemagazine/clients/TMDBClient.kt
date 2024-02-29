package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.ProductionTMDB
import br.com.cinemagazine.dto.tmdb.PageTMDB
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(name = "TMDB", url = "\${tmdb.host}")
interface TMDBClient {
    @GetMapping(path = ["/trending/all/{time_window}"], consumes = [APPLICATION_JSON_VALUE])
    fun trending(
        @PathVariable("time_window") time: String,
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String,
        @RequestParam("page") page: Int
    ): PageTMDB<ProductionTMDB>

    @GetMapping(path = ["/search/movie"], consumes = [APPLICATION_JSON_VALUE])
    fun searchMovie(
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String,
        @RequestParam("query") query: String
    ): PageTMDB<ProductionTMDB>

    @GetMapping(path = ["/search/tv"], consumes = [APPLICATION_JSON_VALUE])
    fun searchTV(
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String,
        @RequestParam("query") query: String
    ): PageTMDB<ProductionTMDB>
}