package br.com.cinemagazine.services

import br.com.cinemagazine.builder.document.getProductionDocument
import br.com.cinemagazine.builder.document.getWatchDocument
import br.com.cinemagazine.builder.watch.getWatchDTO
import br.com.cinemagazine.builder.watch.getWatchProductionDTO
import br.com.cinemagazine.builder.watch.getWatchRequestDTO
import br.com.cinemagazine.constants.ApiMessage
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.documents.WatchDocument
import br.com.cinemagazine.exception.BusinessException
import br.com.cinemagazine.repository.ProductionRepository
import br.com.cinemagazine.repository.WatchRepository
import br.com.cinemagazine.services.impl.WatchServiceImpl
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
import java.util.*

class WatchServiceTest: FunSpec({

    val watchRepository = mockk<WatchRepository>()
    val productionRepository = mockk<ProductionRepository>()
    val service = WatchServiceImpl(watchRepository, productionRepository)

    afterEach { clearAllMocks() }

    test("should add watch a production successfully") {
       every { productionRepository.findByTmdbIdAndMedia(any(Long::class), any(Media::class)) } returns getProductionDocument()
       every { watchRepository.save(any(WatchDocument::class)) } returns getWatchDocument()

        val result = service.addWatchProduction(getWatchRequestDTO())

        result.production.shouldBe(getWatchProductionDTO())
    }

    test("should get production to watch successfully") {
        every { watchRepository.countByUserId(any(String::class)) } returns 1
        every { watchRepository.findByUserId(any(String::class), any(Pageable::class)) } returns PageImpl(listOf(getWatchDocument()))

        val result = service.getWatchProductions("10", PageRequest.of(0, 10))

        result.isFirst.shouldBeTrue()
        result.isLast.shouldBeTrue()
        result.content.shouldBe(listOf(getWatchDTO()))
    }

    test("should delete production to watch successfully") {
        every { watchRepository.findById(any(String::class)) } returns Optional.of(getWatchDocument())
        every { watchRepository.delete(any(WatchDocument::class)) } returns Unit

        service.deleteWatchProduction("1")

        verify { watchRepository.delete(any(WatchDocument::class)) }
    }

    test("should return error when delete production to watch") {
        every { watchRepository.findById(any(String::class)) } returns Optional.empty()

        val exception = shouldThrow<BusinessException> {
            service.deleteWatchProduction("1")
        }

        exception.message?.shouldBe(ApiMessage.WATCH_NOT_FOUND.description)
    }
})