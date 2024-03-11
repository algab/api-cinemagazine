package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.dto.production.CastMovieDTO
import br.com.cinemagazine.dto.production.CastTvDTO
import br.com.cinemagazine.dto.production.CrewMovieDTO
import br.com.cinemagazine.dto.production.CrewTvDTO
import br.com.cinemagazine.dto.production.MovieDTO
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.dto.production.TvDTO
import br.com.cinemagazine.services.ProductionService
import org.springframework.stereotype.Service

@Service
class ProductionServiceImpl(
    private val proxy: TMDBProxy
): ProductionService {

    val jobDirector = "Director"
    val jobWriter = "Writer"
    val jobProducer = "Producer"
    val jobExecutiveProducer = "Executive Producer"

    override fun getProduction(id: Long, media: Media): ProductionDTO {
        if (media == Media.MOVIE) {
            return getMovie(id)
        }
        return getTV(id)
    }

    private fun getMovie(id: Long): MovieDTO {
        val movie = proxy.getMovie(id)
        val credits = proxy.getMovieCredits(id)
        val cast = credits.cast.map { CastMovieDTO(it.name, it.character, it.image) }
        val crew = credits.crew.filter {
            it.job == jobDirector || it.job == jobWriter || it.job == jobProducer || it.job == jobExecutiveProducer
        }.map { CrewMovieDTO(it.name, it.job, it.image) }
        return MovieDTO(
            movie.id,
            movie.title,
            movie.originalTitle,
            movie.description,
            movie.poster!!,
            movie.dateRelease,
            movie.companies.map { it.name }.toList(),
            cast,
            crew
        )
    }

    private fun getTV(id: Long): TvDTO {
        val tv = proxy.getTV(id)
        val credits = proxy.getTVCredits(id)
        val cast = credits.cast.map { CastTvDTO(it.name, it.roles.map { role -> role.character }, it.image) }
        val crew = credits.crew.filter {
            it.jobs.any { data ->
                data.job == jobDirector || data.job == jobWriter || data.job == jobProducer || data.job == jobExecutiveProducer
            }
        }.map { CrewTvDTO(it.name, it.jobs.map { data -> data.job }, it.image) }
        return TvDTO(
            tv.id,
            tv.title,
            tv.originalTitle,
            tv.description,
            tv.poster!!,
            tv.dateRelease,
            tv.companies.map { it.name }.toList(),
            tv.seasons,
            cast,
            crew
        )
    }
}