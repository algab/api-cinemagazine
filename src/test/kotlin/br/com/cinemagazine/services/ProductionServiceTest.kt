package br.com.cinemagazine.services

import br.com.cinemagazine.builder.document.getProductionDocument
import br.com.cinemagazine.builder.tmdb.getCreditMovieTMDB
import br.com.cinemagazine.builder.tmdb.getCreditTvTMDB
import br.com.cinemagazine.builder.tmdb.getCrewMovieTMDB
import br.com.cinemagazine.builder.tmdb.getCrewTvTMDB
import br.com.cinemagazine.builder.tmdb.getJobTvTMDB
import br.com.cinemagazine.builder.tmdb.getMovieTMDB
import br.com.cinemagazine.builder.tmdb.getTvTMDB
import br.com.cinemagazine.clients.TMDBProxy
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.ProductionDocument
import br.com.cinemagazine.dto.production.MovieDTO
import br.com.cinemagazine.dto.production.TvDTO
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.services.impl.ProductionServiceImpl
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class ProductionServiceTest: FunSpec({

    val proxy = mockk<TMDBProxy>()
    val repository = mockk<ProductionRepository>()
    val service = ProductionServiceImpl(proxy, repository)

    afterEach { clearAllMocks() }

    test("should return production from database") {
        val document = getProductionDocument()
        every { repository.findByTmdbAndMedia(any(Long::class), any(Media::class)) } returns getProductionDocument()

        val result = service.getProduction(1, Media.MOVIE)

        result.id.shouldBe(document.tmdb)
        result.title.shouldBe(document.production.title)
        result.shouldBeInstanceOf<MovieDTO>()
    }

    test("should production of type movie") {
        val crew = listOf(
            getCrewMovieTMDB(),
            getCrewMovieTMDB("Writer"),
            getCrewMovieTMDB("Producer"),
            getCrewMovieTMDB("Executive Producer"),
            getCrewMovieTMDB("Coach")
        )
        every { repository.findByTmdbAndMedia(any(Long::class), any(Media::class)) } returns null
        every { proxy.getMovie(any(Long::class)) } returns getMovieTMDB()
        every { proxy.getMovieCredits(any(Long::class)) } returns getCreditMovieTMDB(crew)
        every { repository.save(any(ProductionDocument::class)) } returns getProductionDocument()

        val result = service.getProduction(1, Media.MOVIE)

        result.shouldBeInstanceOf<MovieDTO>()
        result.crew.size.shouldBe(4)
        verify(exactly = 1) { repository.save(any(ProductionDocument::class)) }
    }

    test("should production of type tv") {
        val crew = listOf(
            getCrewTvTMDB(),
            getCrewTvTMDB(listOf(getJobTvTMDB("Producer"))),
            getCrewTvTMDB(listOf(getJobTvTMDB("Executive Producer"))),
            getCrewTvTMDB(listOf(getJobTvTMDB("Director")))
        )
        every { repository.findByTmdbAndMedia(any(Long::class), any(Media::class)) } returns null
        every { proxy.getTV(any(Long::class)) } returns getTvTMDB()
        every { proxy.getTVCredits(any(Long::class)) } returns getCreditTvTMDB(crew)
        every { repository.save(any(ProductionDocument::class)) } returns getProductionDocument()

        val result = service.getProduction(1, Media.TV)

        result.shouldBeInstanceOf<TvDTO>()
        result.crew.size.shouldBe(3)
        verify(exactly = 1) { repository.save(any(ProductionDocument::class)) }
    }

    test("should production of type tv with many cast") {
        every { repository.findByTmdbAndMedia(any(Long::class), any(Media::class)) } returns null
        every { proxy.getTV(any(Long::class)) } returns getTvTMDB()
        every { proxy.getTVCredits(any(Long::class)) } returns getCreditTvTMDB(sizeCast = 25)
        every { repository.save(any(ProductionDocument::class)) } returns getProductionDocument()

        val result = service.getProduction(1, Media.TV)

        result.shouldBeInstanceOf<TvDTO>()
        result.cast.size.shouldBe(20)
        verify(exactly = 1) { repository.save(any(ProductionDocument::class)) }
    }
})