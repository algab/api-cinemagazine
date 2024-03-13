package br.com.cinemagazine.converter

import br.com.cinemagazine.constants.ApiMessage.INVALID_MEDIA_TYPE
import br.com.cinemagazine.constants.Media
import br.com.cinemagazine.exception.BusinessException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe

class StringToMediaConverterTest:FunSpec({

    val converter = StringToMediaConverter()

    test("should return media") {
        val result = converter.convert("movie")

        result.shouldBe(Media.MOVIE)
    }

    test("should return exception") {
        val exception = shouldThrow<BusinessException> {
            converter.convert("mov")
        }
        exception.message.shouldBe(INVALID_MEDIA_TYPE.description)
    }
})