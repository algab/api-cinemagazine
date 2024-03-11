package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.CreditMovieTMDB
import br.com.cinemagazine.dto.tmdb.CreditTvTMDB
import br.com.cinemagazine.dto.tmdb.MovieTMDB
import br.com.cinemagazine.dto.tmdb.PageTMDB
import br.com.cinemagazine.dto.tmdb.SearchTMDB
import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import br.com.cinemagazine.dto.tmdb.TvTMDB
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
    ): PageTMDB<TrendingTMDB>

    @GetMapping(path = ["/search/movie"], consumes = [APPLICATION_JSON_VALUE])
    fun searchMovie(
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String,
        @RequestParam("query") query: String
    ): PageTMDB<SearchTMDB>

    @GetMapping(path = ["/search/tv"], consumes = [APPLICATION_JSON_VALUE])
    fun searchTV(
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String,
        @RequestParam("query") query: String
    ): PageTMDB<SearchTMDB>

    @GetMapping(path = ["/movie/{id}"], consumes = [APPLICATION_JSON_VALUE])
    fun getMovie(
        @PathVariable("id") id: Long,
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String
    ): MovieTMDB

    @GetMapping(path = ["/movie/{id}/credits"], consumes = [APPLICATION_JSON_VALUE])
    fun getMovieCredits(
        @PathVariable("id") id: Long,
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String
    ): CreditMovieTMDB

    @GetMapping(path = ["/tv/{id}"], consumes = [APPLICATION_JSON_VALUE])
    fun getTV(
        @PathVariable("id") id: Long,
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String
    ): TvTMDB

    @GetMapping(path = ["/tv/{id}/aggregate_credits"], consumes = [APPLICATION_JSON_VALUE])
    fun getTVCredits(
        @PathVariable("id") id: Long,
        @RequestParam("api_key") apiKey: String,
        @RequestParam("language") language: String
    ): CreditTvTMDB
}