package br.com.cinemagazine.config

import org.springframework.core.convert.converter.Converter
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.verify
import org.springframework.format.FormatterRegistry

class WebConfigTest:FunSpec({

    val registry = spyk<FormatterRegistry>()
    val webConfig = WebConfig()

    afterEach { clearAllMocks() }

    test("should add new converters") {
        webConfig.addFormatters(registry)

        verify(exactly = 1) { registry.addConverter(any(Converter::class)) }
    }
})