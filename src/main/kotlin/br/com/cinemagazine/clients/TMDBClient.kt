package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.TrendingTMDB
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
        @RequestParam("language") language: String
    ): PageTMDB<TrendingTMDB>
}