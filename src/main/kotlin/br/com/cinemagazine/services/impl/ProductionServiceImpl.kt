package br.com.cinemagazine.services.impl

import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.ProductionDocument
import br.com.cinemagazine.dto.production.CastMovieDTO
import br.com.cinemagazine.dto.production.CastTvDTO
import br.com.cinemagazine.dto.production.CrewMovieDTO
import br.com.cinemagazine.dto.production.CrewTvDTO
import br.com.cinemagazine.dto.production.MovieDTO
import br.com.cinemagazine.dto.production.ProductionDTO
import br.com.cinemagazine.dto.production.RoleTvDTO
import br.com.cinemagazine.dto.production.TvDTO
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.services.ProductionService
import org.bson.types.ObjectId
import org.springframework.stereotype.Service

@Service
class ProductionServiceImpl(
    private val proxy: TMDBProxy,
    private val repository: ProductionRepository
): ProductionService {

    val jobDirector = "Director"
    val jobWriter = "Writer"
    val jobProducer = "Producer"
    val jobExecutiveProducer = "Executive Producer"
    val numberOfCast = 20

    override fun getProduction(id: Long, media: Media): ProductionDTO {
        val document = getDocument(id, media)
        if (document == null) {
            if (media == Media.MOVIE) {
                return getMovie(id)
            }
            return getTV(id)
        }
        return document.production
    }

    private fun getDocument(id: Long, media: Media): ProductionDocument? {
        return this.repository.findByTmdbIdAndMedia(id, media)
    }

    private fun getMovie(id: Long): MovieDTO {
        val movie = proxy.getMovie(id)
        val credits = proxy.getMovieCredits(id)
        val cast = credits.cast.map { CastMovieDTO(it.name, it.character, it.image) }
        val crew = credits.crew.filter {
            it.job == jobDirector || it.job == jobWriter || it.job == jobProducer || it.job == jobExecutiveProducer
        }.map { CrewMovieDTO(it.name, it.job, it.image) }
        val result = MovieDTO(
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
        saveProduction(result, Media.MOVIE)
        return result
    }

    private fun getTV(id: Long): TvDTO {
        val tv = proxy.getTV(id)
        val credits = proxy.getTVCredits(id)
        val finalIndex = if (credits.cast.size >= numberOfCast) numberOfCast else credits.cast.size
        val cast = credits.cast.slice(IntRange(0, finalIndex - 1)).map {
            CastTvDTO(it.name, it.roles.map { role -> RoleTvDTO(role.character, role.totalEpisodes) }, it.image)
        }
        val crew = credits.crew.filter {
            it.jobs.any { data ->
                data.job == jobWriter || data.job == jobProducer || data.job == jobExecutiveProducer
            }
        }.map { CrewTvDTO(it.name, it.jobs.map { data -> data.job }, it.image) }
        val result = TvDTO(
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
        saveProduction(result, Media.TV)
        return result
    }

    private fun saveProduction(production: ProductionDTO, media: Media) {
        this.repository.save(ProductionDocument(ObjectId().toString(), production.id, media, production))
    }
}