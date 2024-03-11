package br.com.cinemagazine.clients

import br.com.cinemagazine.dto.tmdb.CreditMovieTMDB
import br.com.cinemagazine.dto.tmdb.CreditTvTMDB
import br.com.cinemagazine.dto.tmdb.MovieTMDB
import br.com.cinemagazine.dto.tmdb.PageTMDB
import br.com.cinemagazine.dto.tmdb.SearchTMDB
import br.com.cinemagazine.dto.tmdb.TrendingTMDB
import br.com.cinemagazine.dto.tmdb.TvTMDB
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class TMDBProxy(
    private val client: TMDBClient,
    @Value("\${tmdb.key}") private val apiKey: String,
    @Value("\${tmdb.language}") private val language: String
) {
    fun trending(page: Int): PageTMDB<TrendingTMDB> {
        return client.trending("day", apiKey, language, page)
    }

    fun searchMovie(name: String): PageTMDB<SearchTMDB> {
        return client.searchMovie(apiKey, language, name)
    }

    fun searchTV(name: String): PageTMDB<SearchTMDB> {
        return client.searchTV(apiKey, language, name)
    }

    fun getMovie(id: Long): MovieTMDB {
        return client.getMovie(id, apiKey, language)
    }

    fun getMovieCredits(id: Long): CreditMovieTMDB {
        return client.getMovieCredits(id, apiKey, language)
    }

    fun getTV(id: Long): TvTMDB {
        return client.getTV(id, apiKey, language)
    }

    fun getTVCredits(id: Long): CreditTvTMDB {
        return client.getTVCredits(id, apiKey, language)
    }
}