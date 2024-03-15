package br.com.cinemagazine.services

import br.com.cinemagazine.builder.document.getProductionDocument
import br.com.cinemagazine.builder.document.getWatchedDocument
import br.com.cinemagazine.builder.watched.getWatchedDTO
import br.com.cinemagazine.builder.watched.getWatchedProductionDTO
import br.com.cinemagazine.builder.watched.getWatchedRequestDTO
import br.com.cinemagazine.constants.ApiMessage
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.WatchedDocument
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.repository.WatchedRepository
import br.com.cinemagazine.services.impl.WatchedServiceImpl
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.booleans.shouldBeTrue
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import java.util.Optional

class WatchedServiceTest: FunSpec({

    val watchedRepository = mockk<WatchedRepository>()
    val productionRepository = mockk<ProductionRepository>()
    val service = WatchedServiceImpl(watchedRepository, productionRepository)

    afterEach { clearAllMocks() }

    test("should add watched production successfully") {
        every { productionRepository.findByTmdbIdAndMedia(any(Long::class), any(Media::class)) } returns getProductionDocument()
        every { watchedRepository.save(any(WatchedDocument::class)) } returns getWatchedDocument()

        val result = service.addWatchedProduction(getWatchedRequestDTO())

        result.production.shouldBe(getWatchedProductionDTO())
    }

    test("should get productions watched successfully") {
        every { watchedRepository.countByUserId(any(String::class)) } returns 1
        every { watchedRepository.findByUserId(any(String::class), any(Pageable::class)) } returns PageImpl(listOf(getWatchedDocument()))

        val result = service.getWatchedProductions("10", PageRequest.of(0, 10))

        result.isFirst.shouldBeTrue()
        result.isLast.shouldBeTrue()
        result.content.shouldBe(listOf(getWatchedDTO()))
    }

    test("should delete watched production successfully") {
        every { watchedRepository.findById(any(String::class)) } returns Optional.of(getWatchedDocument())
        every { watchedRepository.delete(any(WatchedDocument::class)) } returns Unit

        service.deleteWatchedProduction("1")

        verify { watchedRepository.delete(any(WatchedDocument::class)) }
    }

    test("should return error when delete watched production") {
        every { watchedRepository.findById(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            service.deleteWatchedProduction("1")
        }

        exception.message?.shouldBe(ApiMessage.WATCH_NOT_FOUND.description)
    }
})