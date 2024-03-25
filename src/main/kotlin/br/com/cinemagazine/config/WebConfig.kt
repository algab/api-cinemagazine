package br.com.cinemagazine.config

import br.com.cinemagazine.converter.StringToMediaConverter
import org.springframework.context.annotation.Configuration
import org.springframework.format.FormatterRegistry
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig: WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry
            .addMapping("*")
            .allowedMethods("DELETE", "GET", "POST", "PUT")
            .allowedOrigins("*")
    }

    override fun addFormatters(registry: FormatterRegistry) {
        registry.addConverter(StringToMediaConverter())
    }
}