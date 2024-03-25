package br.com.cinemagazine.config

import org.springframework.core.convert.converter.Converter
import io.kotest.core.spec.style.FunSpec
import io.mockk.clearAllMocks
import io.mockk.spyk
import io.mockk.verify
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry

class WebConfigTest:FunSpec({

    val corsRegistry = spyk<CorsRegistry>()
    val formatterRegistry = spyk<FormatterRegistry>()
    val webConfig = WebConfig()

    afterEach { clearAllMocks() }

    test("should test cors configuration") {
        webConfig.addCorsMappings(corsRegistry)

        verify(exactly = 1) { corsRegistry.addMapping(any()) }
    }

    test("should add new converters") {
        webConfig.addFormatters(formatterRegistry)

        verify(exactly = 1) { formatterRegistry.addConverter(any(Converter::class)) }
    }
})