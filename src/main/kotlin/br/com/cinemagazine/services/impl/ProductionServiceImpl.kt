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
import br.com.cinemagazine.dto.tmdb.CreditMovieTMDB
import br.com.cinemagazine.dto.tmdb.CreditTvTMDB
import br.com.cinemagazine.dto.tmdb.MovieTMDB
import br.com.cinemagazine.dto.tmdb.TvTMDB
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.services.ProductionService
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.bson.types.ObjectId
import org.springframework.stereotype.Service
import java.util.Objects.isNull

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
        if (isNull(document)) {
            if (media == Media.MOVIE) {
                return getMovie(id)
            }
            return getTV(id)
        }
        return document!!.production
    }

    private fun getDocument(id: Long, media: Media): ProductionDocument? {
        return this.repository.findByTmdbIdAndMedia(id, media)
    }

    private fun getMovie(id: Long) = runBlocking {
        val deferredMovie = awaitAll(*listOf(
            async { proxy.getMovie(id) },
            async { proxy.getMovieCredits(id) }
        ).toTypedArray())
        val movie = deferredMovie[0] as MovieTMDB
        val credits = deferredMovie[1] as CreditMovieTMDB
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
        result
    }

    private fun getTV(id: Long) = runBlocking {
        val deferredTv = awaitAll(*listOf(
            async { proxy.getTV(id) },
            async { proxy.getTVCredits(id) }
        ).toTypedArray())
        val tv = deferredTv[0] as TvTMDB
        val credits = deferredTv[1] as CreditTvTMDB
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
        result
    }

    private fun saveProduction(production: ProductionDTO, media: Media) {
        this.repository.save(ProductionDocument(ObjectId().toString(), production.id, media, production))
    }
}